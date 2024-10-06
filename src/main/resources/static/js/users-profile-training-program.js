document.addEventListener('DOMContentLoaded', function() {

    const dayOfWeekSelect = document.getElementById('inputWeekDay');
    const trainingProgramTableContainer = document.getElementById('training-program-table-container');

    function updateDayOfWeekSelect() {

        if (dayOfWeekSelect.value === '') {
            trainingProgramTableContainer.style.display = 'none';
        } else {
            trainingProgramTableContainer.style.display = 'block';
        }
    }

    updateDayOfWeekSelect();
    dayOfWeekSelect.addEventListener('change', updateDayOfWeekSelect);


    function limitThreeNumberInput(inputField) {
        inputField.addEventListener('input', function() {
            const value = this.value;
            if (value.length > 3) {
                this.value = value.substring(0, 3);
            }
        });
    }

    function limitStringInput(inputField) {
        inputField.addEventListener('input', function() {
            const value = this.value;
            if (value.length > 30) {
                this.value = value.substring(0, 30);
            }
        });
    }


    function changeTrainingProgram(response) {

        var tempContainer = $('<div>').html(response);

        var newContent1 = tempContainer.find('#table-container1').html();
        var newContent2 = tempContainer.find('#table-container2').html();

        $('#table-container1').html(newContent1);
        $('#table-container2').html(newContent2);

        const exerciseNameInputs = document.querySelectorAll('.exerciseName');
        const exerciseWeightInputs = document.querySelectorAll('.exerciseWeight');
        const cardioNameInputs = document.querySelectorAll('.cardioName');
        const cardioWeightInputs = document.querySelectorAll('.cardioWeight');

        Array.from(exerciseNameInputs).forEach(inputField => limitStringInput(inputField));
        Array.from(exerciseWeightInputs).forEach(inputField => limitThreeNumberInput(inputField));
        Array.from(cardioNameInputs).forEach(inputField => limitStringInput(inputField));
        Array.from(cardioWeightInputs).forEach(inputField => limitThreeNumberInput(inputField));



    }


    $('#inputWeekDay').change(function() {
        var selectedDay = $(this).val();

        $.ajax({
            url: '/users/profile/training-program',
            method: 'GET',
            data: { dayOfWeek: selectedDay },
            success: function(response) {
                changeTrainingProgram(response);
            },
            error: function(error) {
                console.error("Ошибка AJAX запроса:", error);
            }
        });

        $.ajax({
            url: '/users/profile/trainer/students/' + userId + '/training-program',
            method: 'GET',
            data: { dayOfWeek: selectedDay },
            success: function(response) {
                changeTrainingProgram(response);
            },
            error: function(error) {
                console.error("Ошибка AJAX запроса:", error);
            }
        });
    });





});