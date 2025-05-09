document.addEventListener('DOMContentLoaded', function () {
    const form = document.getElementById('add-form');
    const submitBtn = document.getElementById('submitBtn');
    const successMessage = document.getElementById('successMessage');

    // Parkolóhelyek betöltése
    loadParkingSpots();

    form.addEventListener('submit', function (event) {
        event.preventDefault();
        submitBtn.disabled = true;

        const spotNumber = document.getElementById('spotNumber').value;
        const plate = document.getElementById('licensePlate').value;
        const carType = document.getElementById('carType').value;
        const color = document.getElementById('color').value;

        // Validáció
        if (!validateInputs(spotNumber, plate, carType, color)) {
            submitBtn.disabled = false;
            return;
        }

        fetch('/api/spots', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({
                spotNumber: parseInt(spotNumber),
                licensePlate: plate,
                carType: carType,
                color: color,
                occupied: true
            })
        })
            .then(response => {
                if (!response.ok) {
                    return response.text().then(text => {
                        throw new Error(text || 'Hiba történt')
                    });
                }
                return response.json();
            })
            .then(data => {
                showSuccess(`Sikeresen hozzáadva a ${spotNumber}. parkolóhelyhez!`);
                form.reset();
                loadParkingSpots(); // Frissítjük a parkolóhely listát
            })
            .catch(error => {
                console.error('Hiba:', error);
                showError(error.message);
            })
            .finally(() => {
                submitBtn.disabled = false;
            });
    });

    function loadParkingSpots() {
        fetch('/api/spots')
            .then(response => {
                if (!response.ok) {
                    throw new Error('Hálózati hiba történt');
                }
                return response.json();
            })
            .then(spots => {
                const spotSelect = document.getElementById('spotNumber');
                spotSelect.innerHTML = '<option value="">Válasszon parkolóhelyet</option>';

                if (!spots || spots.length === 0) {
                    spotSelect.innerHTML = '<option value="">Nincsenek parkolóhelyek</option>';
                    return;
                }

                for (let i = 1; i <= 20; i++) {
                    const spot = spots.find(s => s.spotNumber === i);
                    const option = document.createElement('option');
                    option.value = i;
                    option.textContent = `${i}. parkolóhely` + (spot && spot.occupied ? ' (FOGLALT)' : ' (SZABAD)');
                    option.disabled = spot && spot.occupied;
                    spotSelect.appendChild(option);
                }
            })
            .catch(error => {
                console.error('Hiba a parkolóhelyek betöltésekor:', error);
                const spotSelect = document.getElementById('spotNumber');
                spotSelect.innerHTML = '<option value="">Hiba a betöltéskor</option>';
                for (let i = 1; i <= 20; i++) {
                    const option = document.createElement('option');
                    option.value = i;
                    option.textContent = `${i}. parkolóhely`;
                    spotSelect.appendChild(option);
                }
            });
    }

    function validateInputs(spotNumber, plate, carType, color) {
        let isValid = true;

        if (!spotNumber || spotNumber < 1 || spotNumber > 20) {
            document.getElementById('spotError').textContent = 'Érvényes parkolóhely számot válasszon (1-20)';
            document.getElementById('spotError').style.display = 'block';
            isValid = false;
        } else {
            document.getElementById('spotError').style.display = 'none';
        }

        if (!plate || plate.trim().length < 3) {
            document.getElementById('plateError').textContent = 'Érvényes rendszámot adjon meg';
            document.getElementById('plateError').style.display = 'block';
            isValid = false;
        } else {
            document.getElementById('plateError').style.display = 'none';
        }

        if (!carType || carType.trim().length < 2) {
            document.getElementById('typeError').textContent = 'Adja meg az autó típusát';
            document.getElementById('typeError').style.display = 'block';
            isValid = false;
        } else {
            document.getElementById('typeError').style.display = 'none';
        }

        if (!color || color.trim().length < 2) {
            document.getElementById('colorError').textContent = 'Adja meg az autó színét';
            document.getElementById('colorError').style.display = 'block';
            isValid = false;
        } else {
            document.getElementById('colorError').style.display = 'none';
        }

        return isValid;
    }

    function showSuccess(message) {
        successMessage.textContent = message;
        successMessage.style.display = 'block';
        setTimeout(() => {
            successMessage.style.display = 'none';
        }, 5000);
    }

    function showError(message) {
        alert("Hiba: " + message);
    }
});