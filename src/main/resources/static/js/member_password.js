//member_password

document.addEventListener('DOMContentLoaded', function() {

    // 탭 전환 기능
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
    const memId = document.querySelector('input[name="mem_id"]');
    const memEmail = document.querySelector('input[name="mem_email"]');

    // 아이디 실시간 검증
    if (memId) {
        memId.addEventListener('blur', function() {
            validateId(this.value);
        });
        memId.addEventListener('input', function() {
            if (this.value.trim()) {
                clearError('mem_id');
            }
        });
    }

    // 이메일 실시간 검증
    if (memEmail) {
        memEmail.addEventListener('blur', function() {
            validateEmail(this.value);
        });
        memEmail.addEventListener('input', function() {
            if (this.value.trim()) {
                clearError('mem_email');
            }
        });
    }

    //인증기능 시작
    const btnVerify = document.querySelector('.btn-verify');
    const btnOk = document.querySelector('.btn-check');
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

        const memId = emailForm.mem_id.value.trim();
        const email = emailForm.mem_email.value.trim();

        const isIdValid = validateId(memId);
        const isEmailValid = validateEmail(email);

        if (!isIdValid || !isEmailValid) {
            return;
        }

        const jsonData = {
            "id": memId,
            "email": email
        };

        // 버튼 비활성화 및 텍스트 변경
        btnVerify.disabled = true;
        btnVerify.textContent = '전송중...';

        try {
            const response = await fetch('/shoply/member/find/emailPass', {
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
                btnOk.disabled = false;

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
                alert('입력하신 아이디와 이메일이 일치하는 회원정보가 없습니다.');
            }
        } catch(error) {
            console.error('Error:', error);
            btnVerify.disabled = false;
            btnVerify.textContent = '인증번호 받기';
            alert('인증번호 발송 중 오류가 발생했습니다.');
        }
    });

    // 인증번호 확인 버튼 클릭
    btnOk.addEventListener('click', async function(e) {
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

                //인증번호받기 비활성화
                btnVerify.disabled = true;
                btnVerify.textContent = '인증완료';

                // 확인 버튼 비활성화
                btnOk.disabled = true;
                btnOk.textContent = '인증완료';
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

        const memId = emailForm.mem_id.value.trim();
        const email = emailForm.mem_email.value.trim();

        const isIdValid = validateId(memId);
        const isEmailValid = validateEmail(email);

        if (!isIdValid || !isEmailValid) {
            return;
        }

        if(!isEmailVerified) {
            alert('이메일 인증을 완료해주세요.');
            return;
        }

        location.href = `/shoply/member/find/changePassword?mem_id=${memId}`;
    });

    // 취소 버튼
    btnCancel.addEventListener('click', function() {
        location.href = '/shoply/member/login'; // 로그인 페이지로 이동
    });

});

/**
 * 아이디 유효성 검사
 */
function validateId(id) {
    const idPattern = /^[a-zA-Z0-9]{4,20}$/;

    if (!id) {
        showError('mem_id', '아이디를 입력해주세요.');
        return false;
    }

    if (!idPattern.test(id)) {
        showError('mem_id', '아이디는 영문, 숫자 4~20자로 입력해주세요.');
        return false;
    }

    clearError('mem_id');
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


// 비밀번호 변경 페이지 유효성 검사
const passwordChangeForm = document.querySelector('.password-change-form');

if (passwordChangeForm) {
    const memPass = passwordChangeForm.querySelector('input[name="mem_pass"]');
    const memPass2 = passwordChangeForm.querySelector('input[name="mem_pass2"]');
    const btnPasswordSubmit = document.querySelector('.btn-password-submit');
    const btnPasswordCancel = document.querySelector('.btn-password-cancel');
    const passwordHint = document.querySelector('.password-hint-inline');

    // 새 비밀번호 실시간 검증
    if (memPass) {
        memPass.addEventListener('blur', function() {
            validatePassword(this.value);
        });
        memPass.addEventListener('input', function() {
            if (this.value.trim()) {
                clearError('mem_pass');
                // 비밀번호 확인란도 다시 검증
                if (memPass2.value) {
                    validatePasswordConfirm(memPass2.value, this.value);
                }
            }
        });
    }

    // 새 비밀번호 확인 실시간 검증
    if (memPass2) {
        memPass2.addEventListener('blur', function() {
            validatePasswordConfirm(this.value, memPass.value);
        });
        memPass2.addEventListener('input', function() {
            if (this.value.trim()) {
                clearError('mem_pass2');
            }
        });
    }

    // 폼 제출 시 유효성 검사
    if (btnPasswordSubmit) {
        btnPasswordSubmit.addEventListener('click', function(e) {
            e.preventDefault();

            const password = memPass.value.trim();
            const passwordConfirm = memPass2.value.trim();

            const isPasswordValid = validatePassword(password);
            const isPasswordConfirmValid = validatePasswordConfirm(passwordConfirm, password);

            if (!isPasswordValid || !isPasswordConfirmValid) {
                return;
            }

            // 유효성 검사 통과 시 폼 제출
            passwordChangeForm.submit();
        });
    }

    // 취소 버튼
    if (btnPasswordCancel) {
        btnPasswordCancel.addEventListener('click', function() {
            if (confirm('비밀번호 변경을 취소하시겠습니까?')) {
                location.href = '/shoply/member/login';
            }
        });
    }
}

/**
 * 비밀번호 유효성 검사
 */
function validatePassword(password) {
    // 영문, 숫자, 특수문자 포함 8자 이상
    const passwordPattern = /^(?=.*[a-zA-Z])(?=.*\d)(?=.*[!@#$%^&*()_+\-=\[\]{};':"\\|,.<>\/?]).{8,}$/;
    const passwordHint = document.querySelector('.password-hint-inline');

    if (!password) {
        showError('mem_pass', '비밀번호를 입력해주세요.');
        if (passwordHint) passwordHint.style.display = 'none';
        return false;
    }

    if (password.length < 8) {
        showError('mem_pass', '비밀번호는 8자 이상이어야 합니다.');
        if (passwordHint) passwordHint.style.display = 'none';
        return false;
    }

    if (!passwordPattern.test(password)) {
        showError('mem_pass', '영문, 숫자, 특수문자를 모두 포함해야 합니다.');
        if (passwordHint) passwordHint.style.display = 'none';
        return false;
    }

    clearError('mem_pass');
    if (passwordHint) {
        passwordHint.style.display = 'inline';
        passwordHint.style.color = '#00aa00';
        passwordHint.textContent = '사용 가능한 비밀번호입니다.';
    }
    return true;
}

/**
 * 비밀번호 확인 검사
 */
function validatePasswordConfirm(confirmPassword, originalPassword) {
    if (!confirmPassword) {
        showError('mem_pass2', '비밀번호 확인을 입력해주세요.');
        return false;
    }

    if (confirmPassword !== originalPassword) {
        showError('mem_pass2', '비밀번호가 일치하지 않습니다.');
        return false;
    }

    clearError('mem_pass2');
    return true;
}

/**
 * 에러 메시지 표시 (비밀번호 페이지용)
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
    const wrapper = field.closest('.password-input-wrapper');
    const td = field.closest('td');

    if (wrapper) {
        // 힌트 메시지 숨기기
        const hint = wrapper.querySelector('.password-hint-inline');
        if (hint) hint.style.display = 'none';
        wrapper.appendChild(errorDiv);
    } else if (td) {
        td.appendChild(errorDiv);
    }
}

/**
 * 에러 메시지 제거 (비밀번호 페이지용)
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