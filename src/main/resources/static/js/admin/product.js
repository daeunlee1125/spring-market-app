document.addEventListener("DOMContentLoaded", function() {

    // ============ 상품 등록 페이지 전용 ============

    const cate1 = document.querySelector('select[name="cate1_no"]');
    const cate2 = document.querySelector('select[name="cate2_no"]');

    // 카테고리 기능 (register 페이지에만 있음)
    if(cate1 && cate2) {
        cate1.addEventListener('change', async function () {
            try {
                const response = await fetch('/shoply/admin/product/' + cate1.value + '/cate2')

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
                    option.value = item.cate2_no;
                    option.textContent = item.cate2_name;
                    cate2.appendChild(option);
                });
            } catch (error) {
                console.error('Error:', error);
                alert('서버 오류가 발생했습니다.');
            }
        });
    }

    // 옵션 추가/삭제 기능
    const table = document.getElementById("option-table");
    const button = document.getElementById("add-option-btn");

    if(table && button) {
        button.addEventListener("click", function () {
            // 현재 옵션 갯수 세기
            const rows = table.querySelectorAll("tr").length;
            const next = rows + 1;

            // 새 tr 만들기
            const tr = document.createElement("tr");
            tr.innerHTML = `
                <th>옵션${next}</th>
                <td><input type="text" name="optNames" class="input-1"></td>
                <th>옵션${next} 항목</th>
                <td><input type="text" name="optVals" class="input-1"></td>
                <td><button type="button" class="delete-option-btn" style="padding: 5px 10px; cursor: pointer;">삭제</button></td>
            `;
            table.appendChild(tr);

            // 삭제 버튼에 이벤트 리스너 추가
            const deleteBtn = tr.querySelector('.delete-option-btn');
            deleteBtn.addEventListener('click', function () {
                deleteOption(tr);
            });
        });

        // 옵션 삭제 함수
        function deleteOption(row) {
            row.remove();
            reorderOptions();
        }

        // 옵션 번호 재정렬 함수
        function reorderOptions() {
            if(!table) return;

            const rows = table.querySelectorAll("tr");

            rows.forEach((row, index) => {
                const optionNumber = index + 1;

                // 첫 번째 th 업데이트 (옵션 번호)
                const firstTh = row.querySelector('th:first-child');
                firstTh.textContent = `옵션${optionNumber}`;

                // 세 번째 th 업데이트 (옵션 항목)
                const thirdTh = row.querySelector('th:nth-child(3)');
                thirdTh.textContent = `옵션${optionNumber} 항목`;
            });
        }
    }

    // 포인트 자동 계산
    const prodPrice = document.querySelector('[name="prod_price"]');
    const prodPoint = document.querySelector('[name="prod_point"]');

    if(prodPrice && prodPoint) {
        prodPrice.addEventListener('input', function () {
            const price = parseInt(this.value) || 0;
            const point = Math.floor(price * 0.01);
            prodPoint.value = point;
        });
    }

    // 폼 검증 (register 페이지만)
    const form = document.querySelector('form');

    if(form && cate1) {  // cate1이 있으면 register 페이지
        form.addEventListener('submit', function (e) {

            // 1. 1차 분류 체크
            const cate1Value = document.querySelector('[name="cate1_no"]').value;
            if (!cate1Value || cate1Value === '' || cate1Value === 'default') {
                alert('1차 분류를 선택해주세요.');
                e.preventDefault();
                return false;
            }

            // 2. 2차 분류 체크
            const cate2Value = document.querySelector('[name="cate2_no"]').value;
            if (!cate2Value || cate2Value === '' || cate2Value === 'default') {
                alert('2차 분류를 선택해주세요.');
                e.preventDefault();
                return false;
            }

            // 3. 상품명 체크
            const prodName = document.querySelector('[name="prod_name"]').value.trim();
            if (!prodName) {
                alert('상품명을 입력해주세요.');
                document.querySelector('[name="prod_name"]').focus();
                e.preventDefault();
                return false;
            }

            // 4. 기본설명 체크
            const prodInfo = document.querySelector('[name="prod_info"]').value.trim();
            if (!prodInfo) {
                alert('기본설명을 입력해주세요.');
                document.querySelector('[name="prod_info"]').focus();
                e.preventDefault();
                return false;
            }

            // 5. 제조사 체크
            const prodCompany = document.querySelector('[name="prod_company"]').value.trim();
            if (!prodCompany) {
                alert('제조사를 입력해주세요.');
                document.querySelector('[name="prod_company"]').focus();
                e.preventDefault();
                return false;
            }

            // 6. 상품금액 체크
            const priceValue = document.querySelector('[name="prod_price"]').value;
            if (!priceValue || priceValue < 0) {
                alert('상품금액을 올바르게 입력해주세요.');
                document.querySelector('[name="prod_price"]').focus();
                e.preventDefault();
                return false;
            }

            // 7. 할인율 체크
            const prodSale = document.querySelector('[name="prod_sale"]').value;
            if (!prodSale || prodSale < 0 || prodSale > 100) {
                alert('할인율은 0~100 사이로 입력해주세요.');
                document.querySelector('[name="prod_sale"]').focus();
                e.preventDefault();
                return false;
            }

            // 8. 포인트 체크
            const pointValue = document.querySelector('[name="prod_point"]').value;
            if (!pointValue || pointValue < 0) {
                alert('포인트를 올바르게 입력해주세요.');
                document.querySelector('[name="prod_point"]').focus();
                e.preventDefault();
                return false;
            }

            // 9. 재고수량 체크
            const prodStock = document.querySelector('[name="prod_stock"]').value;
            if (!prodStock || prodStock < 0) {
                alert('재고수량을 올바르게 입력해주세요.');
                document.querySelector('[name="prod_stock"]').focus();
                e.preventDefault();
                return false;
            }

            // 10. 배송비 체크
            const prodDelivPrice = document.querySelector('[name="prod_deliv_price"]').value;
            if (!prodDelivPrice || prodDelivPrice < 0) {
                alert('배송비를 올바르게 입력해주세요.');
                document.querySelector('[name="prod_deliv_price"]').focus();
                e.preventDefault();
                return false;
            }

            // 11. 파일 체크
            const file1 = document.querySelector('[name="file1"]').files.length;
            const file2 = document.querySelector('[name="file2"]').files.length;
            const file3 = document.querySelector('[name="file3"]').files.length;
            const file4 = document.querySelector('[name="file4"]').files.length;

            if (file1 === 0) {
                alert('상품목록 이미지를 선택해주세요.');
                e.preventDefault();
                return false;
            }
            if (file2 === 0) {
                alert('상품메인 이미지를 선택해주세요.');
                e.preventDefault();
                return false;
            }
            if (file3 === 0) {
                alert('상품상세 이미지를 선택해주세요.');
                e.preventDefault();
                return false;
            }
            if (file4 === 0) {
                alert('상품상세정보 이미지를 선택해주세요.');
                e.preventDefault();
                return false;
            }

            // 12. 고시정보 체크
            const notVal1 = document.querySelector('[name="not_val1"]').value.trim();
            if (!notVal1) {
                alert('상품상태를 입력해주세요.');
                document.querySelector('[name="not_val1"]').focus();
                e.preventDefault();
                return false;
            }

            const notVal2 = document.querySelector('[name="not_val2"]').value.trim();
            if (!notVal2) {
                alert('부가세 면세여부를 입력해주세요.');
                document.querySelector('[name="not_val2"]').focus();
                e.preventDefault();
                return false;
            }

            const notVal3 = document.querySelector('[name="not_val3"]').value.trim();
            if (!notVal3) {
                alert('영수증 발행을 입력해주세요.');
                document.querySelector('[name="not_val3"]').focus();
                e.preventDefault();
                return false;
            }

            const notVal4 = document.querySelector('[name="not_val4"]').value.trim();
            if (!notVal4) {
                alert('사업자구분을 입력해주세요.');
                document.querySelector('[name="not_val4"]').focus();
                e.preventDefault();
                return false;
            }

            const notVal5 = document.querySelector('[name="not_val5"]').value.trim();
            if (!notVal5) {
                alert('원산지를 입력해주세요.');
                document.querySelector('[name="not_val5"]').focus();
                e.preventDefault();
                return false;
            }

            return true;
        });
    }

    // ============ 상품 목록 페이지 전용 ============

    // 전체 선택/해제
    const selectAll = document.getElementById('selectAll');

    if(selectAll) {
        selectAll.addEventListener('change', function() {
            const checkboxes = document.querySelectorAll('input[name="prodNos"]');
            checkboxes.forEach(function(checkbox) {
                checkbox.checked = selectAll.checked;
            });

            updateDeleteButtonColor();
        });
    }

    //개별 체크박스 이벤트 추가
    const prodCheckboxes = document.querySelectorAll('input[name="prodNos"]');
    prodCheckboxes.forEach(function(checkbox) {
        checkbox.addEventListener('change', function() {
            updateDeleteButtonColor();
        });
    });

    // 삭제 버튼 색상 업데이트
    function updateDeleteButtonColor() {
        const deleteBtn = document.querySelector('.delete-btn');
        if(!deleteBtn) return;

        const checkedBoxes = document.querySelectorAll('input[name="prodNos"]:checked');

        if(checkedBoxes.length > 0) {
            // 체크된 항목이 있으면 빨간색
            deleteBtn.style.backgroundColor = 'red';
        } else {
            // 체크된 항목이 없으면 원래 색상
            deleteBtn.style.backgroundColor = '';
        }
    }

    //페이지 로드 시 초기 상태 설정
    updateDeleteButtonColor();

});

// 선택 삭제 (DOMContentLoaded 밖에 있어야 HTML에서 호출 가능)
function submitDelete() {
    const checked = document.querySelectorAll('input[name="prodNos"]:checked');

    if(checked.length === 0) {
        alert('삭제할 상품을 선택해주세요.');
        return false;
    }

    if(confirm(`선택한 ${checked.length}개 상품을 삭제하시겠습니까?`)) {
        document.getElementById('deleteForm').submit();
    }
}