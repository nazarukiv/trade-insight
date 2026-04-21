async function calculate() {
    const errorBox = document.getElementById("errorBox");
    const resultBox = document.getElementById("result");

    errorBox.innerHTML = "";
    resultBox.innerHTML = "<div class='loader'></div>";

    const data = {
        balance: parseFloat(document.getElementById("balance").value),
        riskPercent: parseFloat(document.getElementById("risk").value),
        entryPrice: parseFloat(document.getElementById("entry").value),
        stopLossPrice: parseFloat(document.getElementById("sl").value),
        symbol: document.getElementById("symbol").value
    };

    try {
        const res = await fetch("/api/calculate", {
            method: "POST",
            headers: {"Content-Type": "application/json"},
            body: JSON.stringify(data)
        });

        const json = await res.json();

        if (!res.ok) {
            showError(json);
            resultBox.innerHTML = "";
            return;
        }

        resultBox.innerHTML = `
            <div class="result">
                Risk: $${json.riskAmount}<br>
                SL: ${json.stopLossTicks}<br>
                Lot: ${json.lotSize}
            </div>
        `;

    } catch (e) {
        showError({error: "Server error"});
    }
}

function showError(errors) {
    const box = document.getElementById("errorBox");

    if (errors.error) {
        box.innerHTML = `<div class="error">⚠ ${errors.error}</div>`;
        return;
    }

    let html = "";
    for (let key in errors) {
        html += `<div class="error">⚠ ${errors[key]}</div>`;
    }

    box.innerHTML = html;
}

async function loadSessions() {
    const box = document.getElementById("sessions");
    box.innerHTML = "<div class='loader'></div>";

    const tz = document.getElementById("timezone").value;

    const res = await fetch(`/api/sessions?tz=${tz}`);
    const data = await res.json();

    let html = "";
    data.forEach(s => {
        html += `<div>${s.name}: ${s.start.substring(11,16)} - ${s.end.substring(11,16)}</div>`;
    });

    box.innerHTML = html;
}

async function loadNews() {
    const box = document.getElementById("news");
    box.innerHTML = "<div class='loader'></div>";

    const res = await fetch("/api/news");
    const data = await res.json();

    let html = "";
    data.forEach(n => {
        html += `<div>${n.time} | ${n.currency} | ${n.event}</div>`;
    });

    box.innerHTML = html;
}