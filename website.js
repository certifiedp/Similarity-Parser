document.addEventListener("DOMContentLoaded", function () {
    document.getElementById("uploadForm").addEventListener("submit", function (e) {
        e.preventDefault();
        const formData = new FormData(this);
        fetch("/checkPlagiarism", {
            method: "POST",
            body: formData
        })
        .then(response => response.json())
        .then(data => {
            document.getElementById("result").innerText = `Similarity: ${data.similarity}%`;
        })
        .catch(error => console.error("Error:", error));
    });
});
