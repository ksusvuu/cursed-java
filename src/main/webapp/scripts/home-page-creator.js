import { getCookieValue } from "./cookie-controller.js";

document.addEventListener("DOMContentLoaded", function () {
    const guiElementsCookie = getCookieValue("guiElements");
    const decodedJson = decodeURIComponent(escape(atob(guiElementsCookie)));
    const guiElements = JSON.parse(decodedJson);

    console.log(guiElements);

    guiElements.forEach(element => {
        var str;
        if (element.includes('.txt'))
            str = element.substring(0, element.indexOf('-'));
        else
            str = element;

        var form = document.createElement('form');
        var content = document.createElement('p');
        content.textContent = str;
        form.appendChild(content);

        form.className = 'button';
        form.method = 'get';

        if (str == 'Ведомости') {
            form.action = '/digital-statement/subjects';
        }
        else if (str == 'Создать ведомость') {
            form.action = '/digital-statement/table-creator';
        }
        else {
            var hiddenInput = document.createElement('input');
            hiddenInput.type = 'hidden';
            hiddenInput.name = 'src';
            hiddenInput.value = element;
            form.appendChild(hiddenInput);
            form.action = '/digital-statement/statement';
        }

        form.addEventListener("click", function (event) {
            event.preventDefault();
            form.submit();
        });

        document.getElementsByTagName('aside')[0].appendChild(form);

        document.getElementById("user-icon").addEventListener('click', function () {
            window.location.href = "/digital-statement/user-info";
        });
    });
});
