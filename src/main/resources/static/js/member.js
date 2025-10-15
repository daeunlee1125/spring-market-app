let isIdChecked = false;
let checkedId = '';
let isEmailVerified = false;

document.addEventListener('DOMContentLoaded', function() {
    const form = document.querySelector('form[action*="/member/register"]:not([action*="Seller"])');
    const sellerForm = document.querySelector('form[action*="/member/registerSeller"]');

    console.log('Form found:', form);
    console.log('SellerForm found:', sellerForm);

    if (!form && !sellerForm) return;

    if (form) {
        // 각 입력 필드에 실시간 검증 이벤트 추가
        const memId = document.querySelector('input[name="mem_id"]');
        const memPass = document.querySelector('input[name="mem_pass"]');
        const memPass2 = document.querySelector('input[name="mem_pass2"]');
        const memName = document.querySelector('input[name="mem_name"]');
        const memEmail = document.querySelector('input[name="mem_email"]');
        const memHp = document.querySelector('input[name="mem_hp"]');

        // 아이디 실시간 검증
        if (memId) {
            memId.addEventListener('blur', function () {
                validateId(this.value);
            });
        }

        // 아이디 입력 필드가 변경되면 중복 확인 초기화
        const memIdField = document.querySelector('input[name="mem_id"]');
        if (memIdField) {
            memIdField.addEventListener('input', function () {
                if (this.value !== checkedId) {
                    isIdChecked = false;
                }
            });
        }

        // 비밀번호 실시간 검증
        if (memPass) {
            memPass.addEventListener('blur', function () {
                validatePassword(this.value);
            });
        }

        // 비밀번호 확인 실시간 검증
        if (memPass2) {
            memPass2.addEventListener('blur', function () {
                validatePasswordConfirm(memPass.value, this.value);
            });
        }

        // 이름 실시간 검증
        if (memName) {
            memName.addEventListener('blur', function() {
                validateName(this.value);
            });
        }

        // 생년월일 실시간 검증
        const memBirth = document.querySelector('input[name="mem_birth"]');
        if (memBirth) {
            memBirth.addEventListener('change', function () {
                validateBirth(this.value);
            });
        }

        // 이메일 실시간 검증
        if (memEmail) {
            memEmail.addEventListener('blur', function () {
                validateEmail(this.value);
            });
        }

        // 휴대폰 번호 자동 하이픈 추가
        if (memHp) {
            memHp.addEventListener('input', function () {
                this.value = formatPhoneNumber(this.value);
            });
            memHp.addEventListener('blur', function () {
                validatePhone(this.value);
            });
        }

        // 폼 제출 시 전체 검증
        form.addEventListener('submit', function (e) {
            if (!validateForm()) {
                e.preventDefault();
                return false;
            }
        });

        const btnVerify = document.querySelector('.btn-verify');
        const btnOk = document.querySelector('.btn-ok');

        if (btnVerify && btnOk) {

            let canResend = true;

            btnVerify.addEventListener('click', async function (e) {
                e.preventDefault();

                if (!canResend) {
                    alert('잠시 후 다시 시도해주세요.');
                    return;
                }

                if (!memEmail.value) {
                    alert('이메일을 입력해주세요.');
                    return;
                }
                const jsonData = {
                    "email": memEmail.value
                };

                // 버튼 비활성화 및 텍스트 변경
                btnVerify.disabled = true;
                btnVerify.textContent = '전송중...';

                try {
                    const response = await fetch('/shoply/member/email', {
                        method: 'POST',
                        headers: {"Content-Type": "application/json"},
                        body: JSON.stringify(jsonData)
                    });

                    if (!response.ok) {
                        throw new Error('서버 응답 오류');
                    }

                    const data = await response.json();

                    if (data.count === 0) {
                        alert('인증번호가 이메일로 발송되었습니다.');
                        form.auth.style.display = 'inline-block';
                        form.auth.disabled = false;
                        btnOk.style.display = 'inline-block';
                        btnOk.disabled = false;

                        // 60초 타이머 시작
                        canResend = false;
                        let countdown = 60;

                        const timer = setInterval(() => {
                            btnVerify.textContent = `재전송 (${countdown}초)`;
                            countdown--;

                            if (countdown < 0) {
                                clearInterval(timer);
                                btnVerify.disabled = false;
                                btnVerify.textContent = '재전송';
                                canResend = true;
                            }
                        }, 1000);

                    } else {
                        btnVerify.disabled = false;
                        btnVerify.textContent = '인증번호 받기';
                        alert('이미 사용중인 이메일 입니다.');
                    }
                } catch (error) {
                    console.error('Error:', error);
                    btnVerify.disabled = false;
                    btnVerify.textContent = '인증번호 받기';
                    alert('인증번호 발송 중 오류가 발생했습니다.');
                }
            });

            // 인증번호 확인 버튼 클릭
            btnOk.addEventListener('click', async function (e) {
                e.preventDefault();

                const code = form.auth.value.trim();
                console.log(code);

                if (!code) {
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

                    if (data.valid) {
                        alert('인증이 완료되었습니다.');
                        isEmailVerified = true;

                        // 이메일과 인증번호 입력란을 읽기 전용으로 변경
                        memEmail.readOnly = true;
                        form.auth.readOnly = true;

                        // 확인 버튼 비활성화
                        btnOk.disabled = true;
                        btnOk.textContent = '인증완료';
                    } else {
                        alert('인증번호가 일치하지 않습니다. 다시 확인해주세요.');
                    }
                } catch (error) {
                    console.error('Error:', error);
                    alert('인증번호 확인 중 오류가 발생했습니다.');
                }
            });
        }
    }

    // ====== 판매자 회원가입 전용 ======
    if (sellerForm) {
        const memId = document.querySelector('input[name="mem_id"]');
        const memPass = document.querySelector('input[name="mem_pass"]');
        const memPass2 = document.querySelector('input[name="mem_pass2"]');
        const corpName = document.querySelector('input[name="corp_name"]');
        const memName = document.querySelector('input[name="mem_name"]');
        const corpRegHp = document.querySelector('input[name="corp_reg_hp"]');
        const corpTelHp = document.querySelector('input[name="corp_tel_hp"]');
        const memHp = document.querySelector('input[name="mem_hp"]');
        const corpFax = document.querySelector('input[name="corp_fax"]');

        // 아이디 실시간 검증
        if (memId) {
            memId.addEventListener('blur', function() {
                validateId(this.value);
            });
            memId.addEventListener('input', function() {
                if (this.value !== checkedId) {
                    isIdChecked = false;
                }
            });
        }

        // 비밀번호 실시간 검증
        if (memPass) {
            memPass.addEventListener('blur', function() {
                validatePassword(this.value);
            });
        }

        // 비밀번호 확인 실시간 검증
        if (memPass2) {
            memPass2.addEventListener('blur', function() {
                validatePasswordConfirm(memPass.value, this.value);
            });
        }

        // 회사명 실시간 검증
        if (corpName) {
            corpName.addEventListener('blur', function() {
                validateCorpName(this.value);
            });
        }

        // 대표 이름 실시간 검증
        if (memName) {
            memName.addEventListener('blur', function() {
                validateName(this.value);
            });
        }

        // 사업자등록번호 자동 하이픈 추가 및 검증
        if (corpRegHp) {
            corpRegHp.addEventListener('input', function() {
                this.value = formatBusinessNumber(this.value);
            });
            corpRegHp.addEventListener('blur', function() {
                validateBusinessNumber(this.value);
            });
        }

        // 통신판매업번호 검증
        if (corpTelHp) {
            corpTelHp.addEventListener('blur', function() {
                validateTelecomNumber(this.value);
            });
        }

        // 전화번호 자동 하이픈 추가 및 검증 (휴대폰 + 일반전화)
        if (memHp) {
            memHp.addEventListener('input', function() {
                this.value = formatPhoneOrTel(this.value);  // 변경
            });
            memHp.addEventListener('blur', function() {
                validatePhoneOrTel(this.value);  // 변경
            });
        }

        // 팩스 자동 하이픈 추가 및 검증
        if (corpFax) {
            corpFax.addEventListener('input', function() {
                this.value = formatPhoneOrTel(this.value);
            });

            corpFax.addEventListener('blur', function() {
                validateFax(this.value);
            });
        }

        // 폼 제출 시 전체 검증
        sellerForm.addEventListener('submit', function(e) {
            if (!validateSellerForm()) {
                e.preventDefault();
                return false;
            }
        });
    }
});

/**
 * 아이디 중복 확인
 */
function checkIdDuplicate() {
    const memId = document.querySelector('input[name="mem_id"]').value;

    // 아이디 유효성 검사 먼저 실행
    if (!validateId(memId)) {
        return;
    }

    // 서버에 중복 확인 요청
    fetch('/shoply/member/checkId?mem_id=' + encodeURIComponent(memId))
        .then(response => response.json())
        .then(data => {
            if (data.result === 'success') {
                alert('사용 가능한 아이디입니다.');
                isIdChecked = true;
                checkedId = memId;
                // 아이디 필드 비활성화 (선택사항)
                // document.querySelector('input[name="mem_id"]').readOnly = true;
            } else {
                alert('이미 사용 중인 아이디입니다.');
                isIdChecked = false;
                checkedId = '';
            }
        })
        .catch(error => {
            console.error('Error:', error);
            alert('중복 확인 중 오류가 발생했습니다.');
        });
}

/**
 * 아이디 유효성 검사
 * 영문, 숫자 4~12자
 */
function validateId(id) {
    const idPattern = /^[a-zA-Z0-9]{4,12}$/;

    if (!id) {
        showError('mem_id', '아이디를 입력해주세요.');
        return false;
    }

    if (!idPattern.test(id)) {
        showError('mem_id', '아이디는 영문, 숫자 4~12자로 입력해주세요.');
        return false;
    }

    clearError('mem_id');
    return true;
}

/**
 * 비밀번호 유효성 검사
 * 영문, 숫자, 특수문자 포함 8~12자
 */
function validatePassword(password) {
    const passwordPattern = /^(?=.*[a-zA-Z])(?=.*\d)(?=.*[@$!%*?&])[A-Za-z\d@$!%*?&]{8,12}$/;

    if (!password) {
        showError('mem_pass', '비밀번호를 입력해주세요.');
        return false;
    }

    if (!passwordPattern.test(password)) {
        showError('mem_pass', '비밀번호는 영문, 숫자, 특수문자를 포함한 8~12자로 입력해주세요.');
        return false;
    }

    clearError('mem_pass');
    return true;
}

/**
 * 비밀번호 확인 검사
 */
function validatePasswordConfirm(password, passwordConfirm) {
    if (!passwordConfirm) {
        showError('mem_pass2', '비밀번호 확인을 입력해주세요.');
        return false;
    }

    if (password !== passwordConfirm) {
        showError('mem_pass2', '비밀번호가 일치하지 않습니다.');
        return false;
    }

    clearError('mem_pass2');
    return true;
}

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
 * 생년월일 유효성 검사
 */
function validateBirth(birth) {
    if (!birth) {
        showError('mem_birth', '생년월일을 입력해주세요.');
        return false;
    }

    const birthDate = new Date(birth);
    const today = new Date();

    // 유효한 날짜인지 확인
    if (isNaN(birthDate.getTime())) {
        showError('mem_birth', '올바른 날짜를 입력해주세요.');
        return false;
    }

    // 미래 날짜 체크
    if (birthDate > today) {
        showError('mem_birth', '미래 날짜는 선택할 수 없습니다.');
        return false;
    }

    // 만 14세 이상 체크
    const age = calculateAge(birthDate);
    if (age < 14) {
        showError('mem_birth', '만 14세 이상만 가입 가능합니다.');
        return false;
    }

    // 너무 오래된 날짜 체크 (예: 120세 이상)
    if (age > 120) {
        showError('mem_birth', '올바른 생년월일을 입력해주세요.');
        return false;
    }

    clearError('mem_birth');
    return true;
}

/**
 * 나이 계산 함수
 */
function calculateAge(birthDate) {
    const today = new Date();
    let age = today.getFullYear() - birthDate.getFullYear();
    const monthDiff = today.getMonth() - birthDate.getMonth();

    if (monthDiff < 0 || (monthDiff === 0 && today.getDate() < birthDate.getDate())) {
        age--;
    }

    return age;
}

/**
 * 성별 유효성 검사
 */
function validateGender() {
    const gender = document.querySelector('input[name="mem_gen"]:checked');

    if (!gender) {
        alert('성별을 선택해주세요.');
        return false;
    }

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
 * 휴대폰 번호 유효성 검사
 */
function validatePhone(phone) {
    const phonePattern = /^01[0-9]-\d{3,4}-\d{4}$/;

    if (!phone) {
        showError('mem_hp', '휴대폰 번호를 입력해주세요.');
        return false;
    }

    if (!phonePattern.test(phone)) {
        showError('mem_hp', '올바른 휴대폰 번호 형식이 아닙니다. (예: 010-1234-5678)');
        return false;
    }

    clearError('mem_hp');
    return true;
}

/**
 * 휴대폰 번호 자동 하이픈 추가
 */
function formatPhoneNumber(value) {
    const numbers = value.replace(/[^0-9]/g, '');

    if (numbers.length <= 3) {
        return numbers;
    } else if (numbers.length <= 7) {
        return numbers.slice(0, 3) + '-' + numbers.slice(3);
    } else if (numbers.length <= 10) {
        return numbers.slice(0, 3) + '-' + numbers.slice(3, 6) + '-' + numbers.slice(6);
    } else {
        return numbers.slice(0, 3) + '-' + numbers.slice(3, 7) + '-' + numbers.slice(7, 11);
    }
}

/**
 * 주소 유효성 검사
 */
function validateAddress(zip, addr1) {
    if (!zip || !addr1) {
        alert('우편번호 검색을 클릭하여 주소를 입력해주세요.');
        return false;
    }
    return true;
}

/**
 * 회사명 유효성 검사
 */
function validateCorpName(corpName) {
    if (!corpName || corpName.trim().length < 2) {
        showError('corp_name', '회사명을 2자 이상 입력해주세요.');
        return false;
    }

    if (corpName.length > 50) {
        showError('corp_name', '회사명은 50자 이하로 입력해주세요.');
        return false;
    }

    clearError('corp_name');
    return true;
}

/**
 * 사업자등록번호 유효성 검사
 * 형식: 000-00-00000 (총 10자리 숫자)
 */
function validateBusinessNumber(businessNumber) {
    const businessPattern = /^\d{3}-\d{2}-\d{5}$/;

    if (!businessNumber) {
        showError('corp_reg_hp', '사업자등록번호를 입력해주세요.');
        return false;
    }

    if (!businessPattern.test(businessNumber)) {
        showError('corp_reg_hp', '올바른 사업자등록번호 형식이 아닙니다. (예: 123-45-67890)');
        return false;
    }

    clearError('corp_reg_hp');
    return true;
}

/**
 * 사업자등록번호 자동 하이픈 추가
 * 형식: 000-00-00000
 */
function formatBusinessNumber(value) {
    const numbers = value.replace(/[^0-9]/g, '');

    if (numbers.length <= 3) {
        return numbers;
    } else if (numbers.length <= 5) {
        return numbers.slice(0, 3) + '-' + numbers.slice(3);
    } else {
        return numbers.slice(0, 3) + '-' + numbers.slice(3, 5) + '-' + numbers.slice(5, 10);
    }
}

/**
 * 통신판매업번호 유효성 검사
 */
function validateTelecomNumber(telecomNumber) {
    if (!telecomNumber || telecomNumber.trim().length === 0) {
        showError('corp_tel_hp', '통신판매업번호를 입력해주세요.');
        return false;
    }

    clearError('corp_tel_hp');
    return true;
}

/**
 * 전화번호 유효성 검사 (휴대폰 + 일반전화 모두 허용)
 * 휴대폰: 010-1234-5678
 * 일반전화: 02-123-4567, 031-1234-5678 등
 */
function validatePhoneOrTel(phone) {
    // 휴대폰 번호 패턴: 010, 011, 016, 017, 018, 019
    const mobilePattern = /^01[0-9]-\d{3,4}-\d{4}$/;

    // 일반 전화번호 패턴: 02, 031~064 등
    const telPattern = /^0(2|[3-6]\d)-\d{3,4}-\d{4}$/;

    if (!phone) {
        showError('mem_hp', '전화번호를 입력해주세요.');
        return false;
    }

    if (!mobilePattern.test(phone) && !telPattern.test(phone)) {
        showError('mem_hp', '올바른 전화번호 형식이 아닙니다. (예: 010-1234-5678 또는 02-123-4567)');
        return false;
    }

    clearError('mem_hp');
    return true;
}

/**
 * 전화번호 자동 하이픈 추가 (휴대폰 + 일반전화 통합)
 */
function formatPhoneOrTel(value) {
    const numbers = value.replace(/[^0-9]/g, '');

    // 길이가 너무 짧으면 그대로 반환
    if (numbers.length <= 2) {
        return numbers;
    }

    // 02로 시작하는 서울 지역번호
    if (numbers.startsWith('02')) {
        if (numbers.length <= 2) {
            return numbers;
        } else if (numbers.length <= 5) {
            return numbers.slice(0, 2) + '-' + numbers.slice(2);
        } else if (numbers.length <= 9) {
            return numbers.slice(0, 2) + '-' + numbers.slice(2, 5) + '-' + numbers.slice(5, 9);
        } else {
            return numbers.slice(0, 2) + '-' + numbers.slice(2, 6) + '-' + numbers.slice(6, 10);
        }
    }
    // 010, 011 등 휴대폰이거나 031, 051 등 지역번호
    else if (numbers.startsWith('0')) {
        if (numbers.length <= 3) {
            return numbers;
        } else if (numbers.length <= 6) {
            return numbers.slice(0, 3) + '-' + numbers.slice(3);
        } else if (numbers.length <= 10) {
            return numbers.slice(0, 3) + '-' + numbers.slice(3, 6) + '-' + numbers.slice(6, 10);
        } else {
            return numbers.slice(0, 3) + '-' + numbers.slice(3, 7) + '-' + numbers.slice(7, 11);
        }
    }

    return value;
}

/**
 * 팩스 번호 유효성 검사 (선택 입력)
 * 입력하지 않으면 통과, 입력한 경우 전화번호 형식 확인
 */
function validateFax(fax) {
    // 입력하지 않은 경우 통과
    if (!fax || fax.trim().length === 0) {
        clearError('corp_fax');
        return true;
    }

    // 휴대폰 번호 패턴: 010, 011, 016, 017, 018, 019
    const mobilePattern = /^01[0-9]-\d{3,4}-\d{4}$/;

    // 일반 전화번호 패턴: 02, 031~064 등
    const telPattern = /^0(2|[3-6]\d)-\d{3,4}-\d{4}$/;

    if (!mobilePattern.test(fax) && !telPattern.test(fax)) {
        showError('corp_fax', '올바른 팩스번호 형식이 아닙니다. (예: 02-123-4567)');
        return false;
    }

    clearError('corp_fax');
    return true;
}


/**
 * 전체 폼 유효성 검사
 */
function validateForm() {
    const memId = document.querySelector('input[name="mem_id"]').value;
    const memPass = document.querySelector('input[name="mem_pass"]').value;
    const memPass2 = document.querySelector('input[name="mem_pass2"]').value;
    const memName = document.querySelector('input[name="mem_name"]').value;
    const memBirth = document.querySelector('input[name="mem_birth"]').value;
    const memEmail = document.querySelector('input[name="mem_email"]').value;
    const memHp = document.querySelector('input[name="mem_hp"]').value;
    const memZip = document.querySelector('input[name="mem_zip"]').value;
    const memAddr1 = document.querySelector('input[name="mem_addr1"]').value;

    // 각 항목 검증
    if (!validateId(memId)) return false;
    if (!validatePassword(memPass)) return false;
    if (!validatePasswordConfirm(memPass, memPass2)) return false;
    if (!validateName(memName)) return false;
    if (!validateBirth(memBirth)) return false;
    if (!validateGender()) return false;
    if (!validateEmail(memEmail)) return false;
    if (!validatePhone(memHp)) return false;
    if (!validateAddress(memZip, memAddr1)) return false;

    // 아이디 중복 확인 체크
    if (!isIdChecked || memId !== checkedId) {
        alert('아이디 중복 확인을 해주세요.');
        return false;
    }

    // 이메일 인증 여부
    if (!isEmailVerified) {
        alert('이메일 인증을 완료해주세요.');
        document.querySelector('input[name="mem_email"]').focus();
        return false;
    }

    return true;
}

function validateSellerForm() {
    const memId = document.querySelector('input[name="mem_id"]').value;
    const memPass = document.querySelector('input[name="mem_pass"]').value;
    const memPass2 = document.querySelector('input[name="mem_pass2"]').value;
    const corpName = document.querySelector('input[name="corp_name"]').value;
    const memName = document.querySelector('input[name="mem_name"]').value;
    const corpRegHp = document.querySelector('input[name="corp_reg_hp"]').value;
    const corpTelHp = document.querySelector('input[name="corp_tel_hp"]').value;
    const memHp = document.querySelector('input[name="mem_hp"]').value;
    const corpFax = document.querySelector('input[name="corp_fax"]').value;
    const memZip = document.querySelector('input[name="mem_zip"]').value;
    const memAddr1 = document.querySelector('input[name="mem_addr1"]').value;

    // 각 항목 검증
    if (!validateId(memId)) return false;
    if (!validatePassword(memPass)) return false;
    if (!validatePasswordConfirm(memPass, memPass2)) return false;
    if (!validateCorpName(corpName)) return false;
    if (!validateName(memName)) return false;
    if (!validateBusinessNumber(corpRegHp)) return false;
    if (!validateTelecomNumber(corpTelHp)) return false;
    if (!validatePhoneOrTel(memHp)) return false;
    if (!validateFax(corpFax)) return false;
    if (!validateAddress(memZip, memAddr1)) return false;

    // 아이디 중복 확인 체크
    if (!isIdChecked || memId !== checkedId) {
        alert('아이디 중복 확인을 해주세요.');
        return false;
    }

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
    const formInput = field.closest('.form-input') || field.closest('.address-inputs');
    if (formInput) {
        formInput.appendChild(errorDiv);
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

//signup.html 동의하기 버튼 클릭시 작동
function checkAgreement() {
    const requiredTerms = ['terms1', 'terms2', 'terms3'];
    const uncheckedRequired = [];

    for (let termId of requiredTerms) {
        if (!document.getElementById(termId).checked) {
            uncheckedRequired.push(termId);
        }
    }

    if (uncheckedRequired.length > 0) {
        alert('필수 약관에 모두 동의해주세요.');
        return false;
    }

    // 버튼의 data-type 속성에서 type 가져오기
    const type = document.getElementById('submitBtn').dataset.type || 'general';
    window.location.href = (type === 'seller') ? '/shoply/member/registerSeller' : '/shoply/member/register';

    return true;
}

//비밀번호 변경알림
const urlParams = new URLSearchParams(window.location.search);
const success = urlParams.get('success');

if (success === 'success') {
    alert('비밀번호 변경이 완료되었습니다.');

    // 주소창에서 파라미터 제거 (뒤로가기 방지)
    history.replaceState({}, document.title, window.location.pathname);
}


// 소셜 로그인 함수들
function loginWithNaver() {
    console.log('네이버 로그인 API 호출');
    alert('네이버 로그인 API를 연동해주세요.');
}

function loginWithKakao() {
    console.log('카카오 로그인 API 호출');
    alert('카카오 로그인 API를 연동해주세요.');
}

function loginWithGoogle() {
    console.log('구글 로그인 API 호출');
    alert('구글 로그인 API를 연동해주세요.');
}

/**
 * 카카오 우편번호 API 함수
 */
//카카오 우편번호 함수
function postCode() {
    new daum.Postcode({
        oncomplete: function(data) {
            // 팝업에서 검색결과 항목을 클릭했을때 실행할 코드를 작성하는 부분.

            // 각 주소의 노출 규칙에 따라 주소를 조합한다.
            // 내려오는 변수가 값이 없는 경우엔 공백('')값을 가지므로, 이를 참고하여 분기 한다.
            var addr = ''; // 주소 변수
            var extraAddr = ''; // 참고항목 변수

            //사용자가 선택한 주소 타입에 따라 해당 주소 값을 가져온다.
            if (data.userSelectedType === 'R') { // 사용자가 도로명 주소를 선택했을 경우
                addr = data.roadAddress;
            } else { // 사용자가 지번 주소를 선택했을 경우(J)
                addr = data.jibunAddress;
            }

            // 사용자가 선택한 주소가 도로명 타입일때 참고항목을 조합한다.
            if(data.userSelectedType === 'R'){
                // 법정동명이 있을 경우 추가한다. (법정리는 제외)
                // 법정동의 경우 마지막 문자가 "동/로/가"로 끝난다.
                if(data.bname !== '' && /[동|로|가]$/g.test(data.bname)){
                    extraAddr += data.bname;
                }
                // 건물명이 있고, 공동주택일 경우 추가한다.
                if(data.buildingName !== '' && data.apartment === 'Y'){
                    extraAddr += (extraAddr !== '' ? ', ' + data.buildingName : data.buildingName);
                }
                // 표시할 참고항목이 있을 경우, 괄호까지 추가한 최종 문자열을 만든다.
                if(extraAddr !== ''){
                    extraAddr = ' (' + extraAddr + ')';
                }
                // 조합된 참고항목을 해당 필드에 넣는다.
                //document.getElementById("sample6_extraAddress").value = extraAddr;

            } else {
                //document.getElementById("sample6_extraAddress").value = '';
            }

            // 우편번호와 주소 정보를 해당 필드에 넣는다.
            document.getElementById('mem_zip').value = data.zonecode;
            document.getElementById("mem_addr1").value = addr;
            // 커서를 상세주소 필드로 이동한다.
            document.getElementById("mem_addr2").focus();
        }
    }).open();
}