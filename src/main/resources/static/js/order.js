document.addEventListener('DOMContentLoaded', function () {

    // =======================================================
    // 🚀 1. 배송지 변경 로직 (이전 3단계 내용)
    // =======================================================
    const changeAddressBtn = document.getElementById('change-address-btn');

    // '배송지 변경' 버튼이 페이지에 존재할 경우에만 이벤트를 연결합니다.
    if (changeAddressBtn) {
        const addressDisplay = document.getElementById('address-display');
        const zipInput = document.getElementById('mem_zip');
        const addr1Input = document.getElementById('mem_addr1');
        // const addr2Input = document.getElementById('mem_addr2'); // 상세주소 필드가 있다면 활성화

        changeAddressBtn.addEventListener('click', function () {
            new daum.Postcode({
                oncomplete: function(data) {
                    let fullAddr = '';
                    let extraAddr = '';

                    if (data.userSelectedType === 'R') {
                        fullAddr = data.roadAddress;
                    } else {
                        fullAddr = data.jibunAddress;
                    }

                    if (data.userSelectedType === 'R') {
                        if (data.bname !== '' && /[동|로|가]$/g.test(data.bname)) {
                            extraAddr += data.bname;
                        }
                        if (data.buildingName !== '' && data.apartment === 'Y') {
                            extraAddr += (extraAddr !== '' ? ', ' + data.buildingName : data.buildingName);
                        }
                        fullAddr += (extraAddr !== '' ? ' (' + extraAddr + ')' : '');
                    }

                    // 화면과 hidden input에 값 채우기
                    zipInput.value = data.zonecode;
                    addr1Input.value = fullAddr;
                    addressDisplay.innerText = `[${data.zonecode}] ${fullAddr}`;

                    // 상세주소 필드가 있다면 비우고 포커스를 줍니다.
                    // addr2Input.value = '';
                    // addr2Input.focus();
                }
            }).open();
        });
    }


    // =======================================================
    // 🚀 2. 최종 결제 로직 (사용자 제공 코드 수정)
    // =======================================================
    const orderForm = document.getElementById('orderForm'); // 결제 정보를 감싸는 form

    // 결제 버튼 클릭 시 실행될 공통 함수
    const handleOrderClick = function(event) {
        // submit의 기본 동작(페이지 새로고침)을 막고, 우리가 원하는 로직을 실행
        event.preventDefault();

        // 💡 실제로는 약관 동의 여부 등을 여기서 확인해야 합니다.
        // const isAgreed = document.getElementById('agreeCheckbox').checked;
        // if (!isAgreed) {
        //     alert('약관에 동의해주세요.');
        //     return;
        // }

        if (confirm('결제를 진행하시겠습니까?')) {
            // alert 대신 form을 실제로 제출하여 서버로 데이터를 보냅니다.
            if(orderForm) {
                orderForm.submit();
            } else {
                alert('결제 폼을 찾을 수 없습니다.');
            }
        }
    };

    // 메인 컨텐츠의 결제 버튼
    const mainOrderBtn = document.getElementById('mainOrderBtn');
    if (mainOrderBtn) {
        mainOrderBtn.addEventListener('click', handleOrderClick);
    }

    // 사이드바의 결제 버튼
    const sideOrderBtn = document.getElementById('sideOrderBtn');
    if (sideOrderBtn) {
        sideOrderBtn.addEventListener('click', handleOrderClick);
    }

});