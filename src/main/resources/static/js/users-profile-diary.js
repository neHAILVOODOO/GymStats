document.addEventListener('DOMContentLoaded', function() {

    document.getElementById('date').addEventListener('input', function() {

    var date = this.value;
    var link = document.getElementById('link');

    if (date === "") {
        date = new Date().toISOString().slice(0, 10);
    }
    link.dataset.href = '/users/profile/diary/' + date;

});

var link = document.getElementById('link');
var date = document.getElementById('date').value;

if (date === "") {
    date = new Date().toISOString().slice(0, 10);
}

link.dataset.href = '/users/profile/diary/' + date;

link.addEventListener('click', function(event) {
    event.preventDefault();
    location.href = this.dataset.href;

});


    const todayWeightInput = document.getElementById('todayWeight');

    todayWeightInput.addEventListener('input', function() {
        const value = this.value;
        if (value.length > 3) {
            this.value = value.substring(0, 3);
        }
    });

        function limitThreeNumberInput(inputField) {
            inputField.addEventListener('input', function() {
                const value = this.value;
                if (value.length > 3) {
                    this.value = value.substring(0, 3);
                }
            });
        }

    function limitFiveNumberInput(inputField) {
        inputField.addEventListener('input', function() {
            const value = this.value;
            if (value.length > 5) {
                this.value = value.substring(0, 5);
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

    const exerciseNameInputs = document.querySelectorAll('.exerciseName');
    exerciseNameInputs.forEach(inputField => limitStringInput(inputField));

    const exerciseWeightInputs = document.querySelectorAll('.exerciseWeight');
    exerciseWeightInputs.forEach(inputField => limitThreeNumberInput(inputField));

    const exerciseSet1Inputs = document.querySelectorAll('.exerciseSet1');
    exerciseSet1Inputs.forEach(inputField => limitThreeNumberInput(inputField));

    const exerciseSet2Inputs = document.querySelectorAll('.exerciseSet2');
    exerciseSet2Inputs.forEach(inputField => limitThreeNumberInput(inputField));

    const exerciseSet3Inputs = document.querySelectorAll('.exerciseSet3');
    exerciseSet3Inputs.forEach(inputField => limitThreeNumberInput(inputField));

    const exerciseSet4Inputs = document.querySelectorAll('.exerciseSet4');
    exerciseSet4Inputs.forEach(inputField => limitThreeNumberInput(inputField));


    const cardioNameInputs = document.querySelectorAll('.cardioName');
    cardioNameInputs.forEach(inputField => limitStringInput(inputField));

    const cardioDistanceInputs = document.querySelectorAll('.cardioDistance');
    cardioDistanceInputs.forEach(inputField => limitFiveNumberInput(inputField));

    const cardioTimeInputs = document.querySelectorAll('.cardioTime');
    cardioTimeInputs.forEach(inputField => limitThreeNumberInput(inputField));

    const cardioWeightInputs = document.querySelectorAll('.cardioWeight');
    cardioWeightInputs.forEach(inputField => limitThreeNumberInput(inputField));

    const cardioCaloriesInputs = document.querySelectorAll('.cardioCalories');
    cardioCaloriesInputs.forEach(inputField => limitThreeNumberInput(inputField));

    const cardioPulseInputs = document.querySelectorAll('.cardioPulse');
    cardioPulseInputs.forEach(inputField => limitThreeNumberInput(inputField));
});
