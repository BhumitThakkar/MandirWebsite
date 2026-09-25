(function () {
    var form = document.getElementById("hall-form");
    if (!form) {
        return;
    }
    form.addEventListener("submit", function () {
        var button = form.querySelector("button[type='submit']");
        if (button) {
            button.disabled = true;
            button.textContent = "Submitting…";
        }
    });
})();
