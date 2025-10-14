document.addEventListener("DOMContentLoaded", function (){

// --- 모달 로직 ---
    const openModalBtn = document.getElementById('openModalBtn');
    const closeModalBtn = document.getElementById('closeModalBtn');
    const modal = document.getElementById('modal');

// '등록하기' 버튼을 클릭하면 모달을 보여줍니다.
    if (openModalBtn) {
        openModalBtn.addEventListener('click', () => {
            modal.classList.remove('hidden');
        });
    }

// 'x' 버튼을 클릭하면 모달을 숨깁니다.
    if (closeModalBtn) {
        closeModalBtn.addEventListener('click', () => {
            modal.classList.add('hidden');
        });
    }

// 모달의 배경(오버레이) 부분을 클릭하면 모달을 숨깁니다.
    if (modal) {
        modal.addEventListener('click', (event) => {
            if (event.target === modal) {
                modal.classList.add('hidden');
            }
        });

    }
// --- Daum 우편번호 서비스 로직 ---
    const searchAddressBtn = document.getElementById('searchAddressBtn');

    function execDaumPostcode() {
        new daum.Postcode({
            oncomplete: function(data) {
                // 팝업에서 검색결과 항목을 클릭했을때 실행할 코드를 작성하는 부분입니다.
                let addr = ''; // 주소 변수

                // 사용자가 선택한 주소 타입에 따라 해당 주소 값을 가져옵니다.
                if (data.userSelectedType === 'R') { // 사용자가 도로명 주소를 선택했을 경우
                    addr = data.roadAddress;
                } else { // 사용자가 지번 주소를 선택했을 경우(J)
                    addr = data.jibunAddress;
                }

                // 우편번호와 주소 정보를 해당 필드에 넣습니다.
                document.getElementById('zipcode').value = data.zonecode;
                document.getElementById("address").value = addr;
                // 커서를 상세주소 필드로 이동시킵니다.
                document.getElementById("address-detail").focus();
            }
        }).open();
    }

// '우편번호 검색' 버튼 클릭 시 함수를 실행합니다.
    if (searchAddressBtn) {
        searchAddressBtn.addEventListener('click', execDaumPostcode);
    }

    const dltBtn = document.getElementById('deleteSelected');
    if (dltBtn){
        dltBtn.addEventListener('click', async () => {
            const checkedBoxes = document.querySelectorAll('.checkOne:checked');
            const ids = Array.from(checkedBoxes).map(cb => cb.dataset.id);


            const confirmDelete = confirm(`${ids.length}개의 항목을 삭제하시겠습니까?`);
            if (!confirmDelete) return;

            // 서버로 JSON 전송
            const res = await fetch('/shoply/admin/shop/delete', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({ mem_ids : ids })
            });

            if (res.ok) {
                alert('삭제되었습니다.');
                location.reload();
            } else {
                alert('삭제 중 오류 발생');
            }
        });
    }

    const rangeBtn = document.getElementById('rangeBtn');
    if (rangeBtn) {
        rangeBtn.addEventListener('change', function() {
            document.getElementById('filterForm').submit();
        });
    }

})