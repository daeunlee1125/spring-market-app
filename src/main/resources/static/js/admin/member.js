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
        searchAddressBtn.addEventListener('click', execDaumPostcode);