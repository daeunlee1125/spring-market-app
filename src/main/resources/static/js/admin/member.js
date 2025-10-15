document.addEventListener("DOMContentLoaded", () => {

    // ✅ openModal 플래그 감지
    const modal = document.getElementById("modal");
    const openModalFlag = document.body.dataset.openModal === 'true';
    if (openModalFlag && modal) {
        modal.classList.remove("hidden");
    }

    // --- 모달 닫기 로직 ---
    document.querySelectorAll(".modal").forEach(modal => {
        modal.addEventListener("click", e => {
            if (e.target.classList.contains("closeModalBtn") || e.target === modal) {
                modal.classList.add("hidden");
            }
        });
    });

    // --- Daum 우편번호 서비스 로직 ---
    const searchAddressBtn = document.getElementById('searchAddressBtn');

    function execDaumPostcode() {
        new daum.Postcode({
            oncomplete: function(data) {
                let addr = '';
                if (data.userSelectedType === 'R') {
                    addr = data.roadAddress;
                } else {
                    addr = data.jibunAddress;
                }
                document.getElementById('zip').value = data.zonecode;
                document.getElementById('addr1').value = addr;
                document.getElementById('addr2').focus();
            }
        }).open();
    }

    if (searchAddressBtn) {
        searchAddressBtn.addEventListener('click', execDaumPostcode);
    }
});
