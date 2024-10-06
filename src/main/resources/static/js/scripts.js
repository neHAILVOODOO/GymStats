document.addEventListener('DOMContentLoaded', function() {
    const roleSelect = document.getElementById('inputRole');
    const trainerSelectContainer = document.getElementById('trainer-select-container');

    function updateTrainerSelect() {

        if (roleSelect.value === 'STUDENT') {
            trainerSelectContainer.style.display = 'block';
        } else {
            trainerSelectContainer.style.display = 'none';
        }
    }


    updateTrainerSelect();
    roleSelect.addEventListener('change', updateTrainerSelect);


});