document.addEventListener("DOMContentLoaded", function() {
    const table = document.getElementById("option-table");
    const button = document.getElementById("add-option-btn");

    button.addEventListener("click", function() {
        // 현재 옵션 갯수 세기
        const rows = table.querySelectorAll("tr").length;
        const next = rows + 1; // 다음 옵션 번호

        // 새 tr 만들기
        const tr = document.createElement("tr");
        tr.innerHTML = `
            <th>옵션${next}</th>
            <td><input type="text" name="option${next}" class="input-1"></td>
            <th>옵션${next} 항목</th>
            <td><input type="text" name="option${next}_items" class="input-1"></td>
        `;
        table.appendChild(tr);
    });
});