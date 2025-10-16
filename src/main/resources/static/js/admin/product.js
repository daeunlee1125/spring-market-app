document.addEventListener("DOMContentLoaded", function() {

    const cate1 = document.querySelector('select[name="cate1_no"]');
    const cate2 = document.querySelector('select[name="cate2_no"]');

    cate1.addEventListener('change', async function (){
            try{
                const response = await fetch('/shoply/admin/product/'+ cate1.value + '/cate2')

                if (!response.ok) {
                    throw new Error('서버 응답 오류');
                }

                const cate2List = await response.json();
                console.log(cate2List);

                // 기존 option들을 제거 (첫 번째 "선택" 옵션만 남김)
                cate2.innerHTML = '<option value="">-- 2차분류 선택 --</option>';

                // 받아온 데이터로 option 추가
                cate2List.forEach(item => {
                    const option = document.createElement('option');
                    option.value = item.cate2_no;        // value는 cate2_no
                    option.textContent = item.cate2_name; // 화면에 보이는 텍스트는 cate2_name
                    cate2.appendChild(option);
                });
            } catch(error) {
                console.error('Error:', error);
                alert('서버 오류가 발생했습니다.');
            }
        });



    //옵션 추가기능
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
            <td><input type="text" name="optNames" class="input-1"></td>
            <th>옵션${next} 항목</th>
            <td><input type="text" name="optVals" class="input-1"></td>
        `;
        table.appendChild(tr);
    });
});