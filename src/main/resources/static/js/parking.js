const cacheBuster = () => `?_=${new Date().getTime()}`;

document.addEventListener('DOMContentLoaded', function() {
    loadParkingSpots();
    setInterval(loadParkingSpots, 5000);
});

function forceRefresh() {
    console.log("Kényszerített frissítés...");
    loadParkingSpots();
}

function loadParkingSpots() {
    fetch(`/api/spots${cacheBuster()}`)
        .then(response => {
            if (!response.ok) throw new Error('Hálózati hiba történt');
            return response.json();
        })
        .then(spots => {
            updateLastUpdatedTime();
            const list = document.getElementById('status-list');
            list.innerHTML = '';

            if (!spots || spots.length === 0) {
                list.innerHTML = '<div class="error">Nincsenek parkolóhely adatok</div>';
                return;
            }

            for (let i = 1; i <= 20; i++) {
                const spot = spots.find(s => s.spotNumber === i);
                const div = document.createElement('div');
                div.className = 'spot';

                if (spot && spot.occupied) {
                    div.classList.add('occupied');
                    div.innerHTML = `
                        <span class="spot-number">${i}. hely</span>
                        <span class="spot-info">
                            <span class="occupied-spot">FOGLALT</span>
                            <div class="car-details">
                                Rendszám: <strong>${spot.licensePlate || 'Nincs megadva'}</strong><br>
                                Típus: ${spot.carType || 'Nincs megadva'}<br>
                                Szín: ${spot.color || 'Nincs megadva'}
                            </div>
                            <div class="action-buttons">
                                <button class="action-btn edit-btn" onclick="editSpot(${spot.id})">Szerkesztés</button>
                                <button class="action-btn delete-btn" onclick="deleteSpot(${spot.id})">Törlés</button>
                            </div>
                        </span>
                    `;
                } else {
                    div.classList.add('free');
                    div.innerHTML = `
                        <span class="spot-number">${i}. hely</span>
                        <span class="spot-info">
                            <span class="free-spot">SZABAD</span>
                        </span>
                    `;
                }

                list.appendChild(div);
            }
        })
        .catch(error => {
            console.error('Hiba:', error);
            document.getElementById('status-list').innerHTML = `
                <div class="error">Hiba történt: ${error.message}</div>
            `;
        });
}

function editSpot(id) {
    fetch(`/api/spots/${id}${cacheBuster()}`)
        .then(response => response.json())
        .then(spot => {
            document.getElementById('edit-id').value = spot.id;
            document.getElementById('edit-licensePlate').value = spot.licensePlate || '';
            document.getElementById('edit-carType').value = spot.carType || '';
            document.getElementById('edit-color').value = spot.color || '';
            document.getElementById('edit-occupied').value = spot.occupied;

            document.getElementById('edit-modal').style.display = 'block';
        })
        .catch(err => alert("Hiba történt az adat betöltése során."));
}

function closeEditModal() {
    document.getElementById('edit-modal').style.display = 'none';
}

document.getElementById('edit-form').addEventListener('submit', function(e) {
    e.preventDefault();

    const id = document.getElementById('edit-id').value;
    const updatedData = {
        licensePlate: document.getElementById('edit-licensePlate').value,
        carType: document.getElementById('edit-carType').value,
        color: document.getElementById('edit-color').value,
        occupied: document.getElementById('edit-occupied').value === 'true'
    };

    fetch(`/api/spots/${id}`, {
        method: 'PUT',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(updatedData)
    })
    .then(response => {
        if (!response.ok) throw new Error('Sikertelen mentés');
        return response.json();
    })
    .then(() => {
        closeEditModal();
        loadParkingSpots(); // Frissítjük a listát
    })
    .catch(err => alert("Hiba történt a mentés során: " + err.message));
});

function deleteSpot(id) {
    if (confirm('Biztosan törölni szeretnéd ezt a parkolóhelyet?')) {
        fetch(`/api/spots/${id}`, {
            method: 'DELETE'
        })
        .then(response => {
            if (!response.ok) throw new Error('Törlés sikertelen');
            loadParkingSpots();
        })
        .catch(error => {
            console.error('Törlés hiba:', error);
            alert(`Hiba történt a törlés során: ${error.message}`);
        });
    }
}

function updateLastUpdatedTime() {
    const now = new Date();
    document.getElementById('update-time').textContent = now.toLocaleTimeString('hu-HU');
}