document.addEventListener('DOMContentLoaded', function() {


    function limitTwoNumberInput(inputField) {
        inputField.addEventListener('input', function() {
            const value = this.value;
            if (value.length > 2) {
                this.value = value.substring(0, 2);
            }
        });
    }


    const experienceInputs = document.querySelectorAll('.experience');
    experienceInputs.forEach(inputField => limitTwoNumberInput(inputField));



});