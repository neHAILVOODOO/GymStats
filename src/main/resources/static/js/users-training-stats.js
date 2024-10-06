document.addEventListener('DOMContentLoaded', function() {

    const inputTrainingType = document.getElementById('inputTrainingType');
    const inputExerciseName = document.getElementById('inputExerciseName');
    const inputExerciseParameter = document.getElementById('inputExerciseParameter');
    const inputCardioName = document.getElementById('inputCardioName');
    const inputCardioParameter = document.getElementById('inputCardioParameter');

    const inputExerciseNameContainer = document.getElementById('inputExerciseNameContainer');
    const inputExerciseParameterContainer = document.getElementById('inputExerciseParameterContainer');
    const inputCardioNameContainer = document.getElementById('inputCardioNameContainer');
    const inputCardioParameterContainer = document.getElementById('inputCardioParameterContainer');

    const averageBarContainer = document.getElementById('averageBar');

    function inputTrainingHideElements(inputTraining) {

        destroyChart('myChart');
        averageBarContainer.style.display = 'none';

        if (inputTraining.value === 'EXERCISE') {

            inputExerciseNameContainer.style.display = 'block';

            inputCardioNameContainer.style.display = 'none';
            inputCardioParameterContainer.style.display = 'none';

            inputCardioName.value = '';
            inputCardioParameter.value = '';


        } else if (inputTraining.value === 'CARDIO'){

            inputCardioNameContainer.style.display = 'block';

            inputExerciseNameContainer.style.display = 'none';
            inputExerciseParameterContainer.style.display = 'none';

            inputExerciseName.value = '';
            inputExerciseParameter.value = '';

        } else if (inputTraining.value === '') {
            inputExerciseNameContainer.style.display = 'none';
            inputExerciseParameterContainer.style.display = 'none';
            inputCardioNameContainer.style.display = 'none';
            inputCardioParameterContainer.style.display = 'none';

            inputCardioName.value = '';
            inputCardioParameter.value = '';
            inputExerciseName.value = '';
            inputExerciseParameter.value = '';
        }

    }

    function hideOrShowElement(inputElement, hidingElement) {
        if (inputElement.value === '') {
            hidingElement.style.display = 'none';
        } else {
            hidingElement.style.display = 'block';
        }
    }

    function hideElement(inputElement, hidingElement) {
        if (inputElement.value === '') {
            hidingElement.style.display = 'none';
        }
    }


    function makeChangingParameterEmpty(changingParameter) {
        if (inputElement.value === '') {
        changingParameter.value = '';
        }
    }

    inputTrainingType.addEventListener('change', () => {
        inputTrainingHideElements(inputTrainingType);
    });

    inputExerciseName.addEventListener('change', () => {
        hideOrShowElement(inputExerciseName, inputExerciseParameterContainer);
        hideElement(inputExerciseName, inputExerciseParameterContainer);
        makeChangingParameterEmpty(inputExerciseParameter);
    });

    inputCardioName.addEventListener('change', () => {
        hideOrShowElement(inputCardioName, inputCardioParameterContainer,);
        hideElement(inputCardioName, inputCardioParameterContainer);
        makeChangingParameterEmpty(inputCardioParameter);
    });

    inputCardioParameter.addEventListener('change', () => {
        hideOrShowElement(inputCardioParameter, averageBarContainer)
    });

    inputExerciseParameter.addEventListener('change', () => {
        hideOrShowElement(inputExerciseParameter, averageBarContainer)
    });




    $(inputExerciseName).change(function() {
        var selectedTrainingType = $(inputTrainingType).val();
        var selectedExerciseName = $(this).val();
        var selectedExerciseParameter = $(inputExerciseParameter).val();

        var data = { inputExerciseName: selectedExerciseName, inputTrainingType: selectedTrainingType};


        if (selectedExerciseParameter !== null && selectedExerciseParameter !== '') {
            data.inputExerciseParameter = selectedExerciseParameter;
        }

        $.ajax({
            url: '/users/profile/training-stats',
            method: 'GET',
            data: data,
            success: function(response) {

                updateByExercisesByName(selectedExerciseParameter, selectedExerciseName, response);


            },
            error: function(error) {
                console.error("Ошибка AJAX запроса:", error);
            }
        });


    });

    $(inputExerciseParameter).change(function() {
        var selectedTrainingType = $(inputTrainingType).val();
        var selectedExerciseName = $(inputExerciseName).val();
        var selectedExerciseParameter = $(this).val();

        var data = { inputExerciseParameter: selectedExerciseParameter, inputTrainingType: selectedTrainingType};

        if (selectedExerciseName !== null && selectedExerciseName !== '') {
            data.inputExerciseName = selectedExerciseName;
        }

        $.ajax({
            url: '/users/profile/training-stats',
            method: 'GET',
            data: data,
            success: function(response) {

               updateByExercisesByName(selectedExerciseParameter, selectedExerciseName, response);

            },
            error: function(error) {
                console.error("Ошибка AJAX запроса:", error);
            }
        });


    });



    $(inputCardioName).change(function() {
        var selectedTrainingType = $(inputTrainingType).val();
        var selectedCardioName = $(this).val();
        var selectedCardioParameter = $(inputCardioParameter).val();

        var data = { inputCardioName: selectedCardioName, inputTrainingType: selectedTrainingType};


        if (selectedCardioParameter !== null && selectedCardioParameter !== '') {
            data.inputCardioParameter = selectedCardioParameter;
        }

        $.ajax({
            url: '/users/profile/training-stats',
            method: 'GET',
            data: data,
            success: function(response) {

                updateByCardiosByName(selectedCardioParameter, selectedCardioName, response);

            },
            error: function(error) {
                console.error("Ошибка AJAX запроса:", error);
            }
        });


    });

    $(inputCardioParameter).change(function() {
        var selectedTrainingType = $(inputTrainingType).val();
        var selectedCardioName = $(inputCardioName).val();
        var selectedCardioParameter = $(this).val();


        var data = { inputCardioParameter: selectedCardioParameter, inputTrainingType: selectedTrainingType};

        if (selectedCardioName !== null && selectedCardioName !== '') {
            data.inputCardioName = selectedCardioName;
        }

        $.ajax({
            url: '/users/profile/training-stats',
            method: 'GET',
            data: data,
            success: function(response) {

              updateByCardiosByName(selectedCardioParameter, selectedCardioName, response);

            },
            error: function(error) {
                console.error("Ошибка AJAX запроса:", error);
            }
        });

      


    });



    function createChart(chartId, datesArray, data, parameter) {
        const ctx = document.getElementById(chartId).getContext('2d');

       destroyChart(chartId);

         myChart = new Chart(ctx, {
            type: 'bar',
            data: {
                labels: datesArray,
                datasets: [{
                    label: parameter,
                    data: data,
                    backgroundColor: [
                        'rgba(54, 162, 235, 0.2)',
                    ],
                    borderColor: [
                        'rgba(54, 162, 235, 1)',
                    ],
                    borderWidth: 1
                }]
            },
            options: {
                scales: {
                    y: {
                        beginAtZero: true
                    }
                }
            }
        });

        return myChart;
    }

    function destroyChart(chartId) {
        let myChart;

        if (Chart.getChart(chartId) !== undefined) {
            myChart = Chart.getChart(chartId).destroy();
        }
    }

    function updateByCardiosByName(selectedCardioParameter, selectedCardioName, response) {

        if (selectedCardioParameter !== '' && selectedCardioName !== '') {

            const parser = new DOMParser();
            const htmlDoc = parser.parseFromString(response, 'text/html');

            const scriptTag = htmlDoc.querySelector('script#script');
            if (scriptTag) {
                const scriptCode = scriptTag.textContent;

                try {
                    eval(scriptCode);
                    datesArray = [];
                    paramArray = [];
                    average = 0;

                    for (i = 0; i < cardiosByName.length; i++) {
                        datesArray.push(cardiosByName[i].training.trainingDate)
                        paramArray.push(cardiosByName[i][parameter]);
                        average += cardiosByName[i][parameter];
                    }

                    average = Math.floor(average/cardiosByName.length);

                    const chart = createChart('myChart', datesArray, paramArray, parameter);

                    $('#averageBar').html(' <h1 style="font-size: 26px; text-align: left; vertical-align: top; ">В среднем: ' + average + '</h1>');

                } catch (error) {
                    console.error('Ошибка выполнения JavaScript-кода:', error);
                }
            } else {
                console.error('Не найден <script> тег');
            }
        } else {
            destroyChart('myChart');
        }
    }

    function updateByExercisesByName(selectedExerciseParameter, selectedExerciseName, response) {

        if (selectedExerciseName !== '' && selectedExerciseParameter !== '') {

            const parser = new DOMParser();
            const htmlDoc = parser.parseFromString(response, 'text/html');

            const scriptTag = htmlDoc.querySelector('script#script');
            if (scriptTag) {
                const scriptCode = scriptTag.textContent;

                try {
                    eval(scriptCode);
                    datesArray = [];
                    paramArray = [];
                    average = 0;

                    for (i = 0; i < exercisesByName.length; i++) {
                        datesArray.push(exercisesByName[i].training.trainingDate)
                        paramArray.push(exercisesByName[i][parameter]);
                        average += exercisesByName[i][parameter];
                    }

                    average = Math.floor(average/exercisesByName.length);

                    const chart = createChart('myChart', datesArray, paramArray, parameter);

                    $('#averageBar').html(' <h1 style="font-size: 26px; text-align: left; vertical-align: top; ">В среднем: ' + average + '</h1>');

                } catch (error) {
                    console.error('Ошибка выполнения JavaScript-кода:', error);
                }
            } else {
                console.error('Не найден <script> тег');
            }
        } else {
            destroyChart('myChart');
        }

    }


});