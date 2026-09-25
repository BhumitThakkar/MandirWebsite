(function () {
    var form = document.getElementById("admin-hall-form");
    if (!form) {
        return;
    }
    form.addEventListener("submit", function () {
        var button = form.querySelector("button[type='submit']");
        if (button) {
            button.disabled = true;
        }
    });
})();
