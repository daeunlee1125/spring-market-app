//member_find
// 탭 전환 기능
document.addEventListener('DOMContentLoaded', function() {
    const tabButtons = document.querySelectorAll('.tab-button');
    const tabContents = document.querySelectorAll('.tab-content');

    tabButtons.forEach(button => {
        button.addEventListener('click', function() {
            const targetTab = this.getAttribute('data-tab');

            // 모든 탭 버튼 비활성화
            tabButtons.forEach(btn => btn.classList.remove('active'));
            // 현재 클릭된 탭 버튼 활성화
            this.classList.add('active');

            // 모든 탭 콘텐츠 숨기기
            tabContents.forEach(content => content.classList.remove('active'));
            // 해당 탭 콘텐츠 보이기
            document.getElementById(targetTab + '-tab').classList.add('active');
        });
    });

    // 각 입력 필드에 실시간 검증 이벤트 추가
    const memName = document.querySelector('input[name="mem_name"]');
    const memEmail = document.querySelector('input[name="mem_email"]');

    // 이름 실시간 검증
    if (memName) {
        memName.addEventListener('blur', function() {
            validateName(this.value);
        });

        memName.addEventListener('input', function() {
            if (this.value.trim()) {
                clearError('mem_name');
            }
        });
    }

    // 이메일 실시간 검증
    if (memEmail) {
        memEmail.addEventListener('blur', function () {
            validateEmail(this.value);
        });

        memEmail.addEventListener('input', function() {
            if (this.value.trim()) {
                clearError('mem_email');
            }
        });
    }

    const btnVerify = document.querySelector('.btn-verify');
    const btnCheck = document.querySelector('.btn-check');
    const btnSubmit = document.querySelector('.btn-submit');
    const btnCancel = document.querySelector('.btn-cancel');
    const emailForm = document.querySelector('form[name="emailForm"]');

    let isEmailVerified = false; // 인증 완료 여부
    let canResend = true;

    // 인증번호 받기 버튼 클릭
    btnVerify.addEventListener('click', async function(e) {
        e.preventDefault();

        if(!canResend) {
            alert('잠시 후 다시 시도해주세요.');
            return;
        }

        const name = emailForm.mem_name.value.trim();
        const email = emailForm.mem_email.value.trim();

        const isNameValid = validateName(name);
        const isEmailValid = validateEmail(email);

        if (!isNameValid || !isEmailValid) {
            return;
        }


        if(!name) {
            alert('이름을 입력해주세요.');
            return;
        }

        if(!email) {
            alert('이메일을 입력해주세요.');
            return;
        }

        const jsonData = {
            "name": name,
            "email": email
        };

        // 버튼 비활성화 및 텍스트 변경
        btnVerify.disabled = true;
        btnVerify.textContent = '전송중...';

        try {
            const response = await fetch('/shoply/member/find/email', {
                method: 'POST',
                headers: {"Content-Type": "application/json"},
                body: JSON.stringify(jsonData)
            });

            if (!response.ok) {
                throw new Error('서버 응답 오류');
            }

            const data = await response.json();

            if(data.count > 0) {
                alert('인증번호가 이메일로 발송되었습니다.');
                emailForm.auth.disabled = false;
                btnCheck.disabled = false;

                // 60초 타이머 시작
                canResend = false;
                let countdown = 60;

                const timer = setInterval(() => {
                    btnVerify.textContent = `재전송 (${countdown}초)`;
                    countdown--;

                    if(countdown < 0) {
                        clearInterval(timer);
                        btnVerify.disabled = false;
                        btnVerify.textContent = '재전송';
                        canResend = true;
                    }
                }, 1000);

            } else {
                btnVerify.disabled = false;
                btnVerify.textContent = '인증번호 받기';
                alert('입력하신 이름과 이메일이 일치하는 회원정보가 없습니다.');
            }
        } catch(error) {
            console.error('Error:', error);
            btnVerify.disabled = false;
            btnVerify.textContent = '인증번호 받기';
            alert('인증번호 발송 중 오류가 발생했습니다.');
        }
    });

    // 인증번호 확인 버튼 클릭
    btnCheck.addEventListener('click', async function(e) {
        e.preventDefault();

        const code = emailForm.auth.value.trim();

        if(!code) {
            alert('인증번호를 입력해주세요.');
            return;
        }

        const jsonData = {
            "code": code
        };

        try {
            const response = await fetch('/shoply/member/find/verifyCode', {
                method: 'POST',
                headers: {"Content-Type": "application/json"},
                body: JSON.stringify(jsonData)
            });

            const data = await response.json();

            if(data.valid) {
                alert('인증이 완료되었습니다.');
                isEmailVerified = true;

                // 확인 버튼 비활성화
                btnCheck.disabled = true;
                btnCheck.textContent = '인증완료';
            } else {
                alert('인증번호가 일치하지 않습니다. 다시 확인해주세요.');
            }
        } catch(error) {
            console.error('Error:', error);
            alert('인증번호 확인 중 오류가 발생했습니다.');
        }
    });

    // 다음 버튼 클릭
    btnSubmit.addEventListener('click', function(e) {
        e.preventDefault();

        const name = emailForm.mem_name.value.trim();
        const email = emailForm.mem_email.value.trim();

        const isNameValid = validateName(name);
        const isEmailValid = validateEmail(email);

        if (!isNameValid || !isEmailValid) {
            return;
        }

        if(!isEmailVerified) {
            alert('이메일 인증을 완료해주세요.');
            return;
        }

        emailForm.submit();
    });

    // 취소 버튼
    btnCancel.addEventListener('click', function() {
        location.href = '/shoply/member/login'; // 로그인 페이지로 이동
    });

});

/**
 * 이름 유효성 검사
 */
function validateName(name) {
    const namePattern = /^[가-힣a-zA-Z]{2,20}$/;

    if (!name) {
        showError('mem_name', '이름을 입력해주세요.');
        return false;
    }

    if (!namePattern.test(name)) {
        showError('mem_name', '이름은 한글 또는 영문 2~20자로 입력해주세요.');
        return false;
    }

    clearError('mem_name');
    return true;
}

/**
 * 이메일 유효성 검사
 */
function validateEmail(email) {
    const emailPattern = /^[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/;

    if (!email) {
        showError('mem_email', '이메일을 입력해주세요.');
        return false;
    }

    if (!emailPattern.test(email)) {
        showError('mem_email', '올바른 이메일 형식이 아닙니다.');
        return false;
    }

    clearError('mem_email');
    return true;
}


/**
 * 에러 메시지 표시
 */
function showError(fieldName, message) {
    const field = document.querySelector(`input[name="${fieldName}"]`);
    if (!field) return;

    // 기존 에러 메시지 제거
    clearError(fieldName);

    // 에러 메시지 생성
    const errorDiv = document.createElement('div');
    errorDiv.className = 'error-message';
    errorDiv.style.color = '#ff0000';
    errorDiv.style.fontSize = '12px';
    errorDiv.style.marginTop = '5px';
    errorDiv.textContent = message;
    errorDiv.setAttribute('data-error-for', fieldName);

    // 필드에 에러 스타일 추가
    field.style.borderColor = '#ff0000';

    // 에러 메시지 삽입
    const formGroup = field.closest('.form-group');
    if (formGroup) {
        formGroup.appendChild(errorDiv);
    } else {
        // form-group이 없으면 input-group 다음에 삽입
        const inputGroup = field.closest('.input-group');
        if (inputGroup) {
            inputGroup.parentNode.insertBefore(errorDiv, inputGroup.nextSibling);
        }
    }
}

/**
 * 에러 메시지 제거
 */
function clearError(fieldName) {
    const field = document.querySelector(`input[name="${fieldName}"]`);
    if (!field) return;

    // 필드 스타일 복구
    field.style.borderColor = '';

    // 에러 메시지 제거
    const errorMsg = document.querySelector(`[data-error-for="${fieldName}"]`);
    if (errorMsg) {
        errorMsg.remove();
    }
}