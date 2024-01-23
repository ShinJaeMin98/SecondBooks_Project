window.addEventListener("DOMContentLoaded", function() {
    // 댓글 작성 후 URL에 comment_id=댓글 등록번호 -> hash 추가 -> 이동
    if (location.search.indexOf("comment_id=") != -1) {
        const searchParams = new URLSearchParams(location.search);

        const seq = searchParams.get("comment_id");

        location.hash=`#comment_${seq}`;
    }

    /* 댓글 수정 버튼 클릭 처리 S */
    const editComments = document.getElementsByClassName("edit_comment");
    for (const el of editComments) {
        el.addEventListener("click", function() {
            const dataset = this.dataset;
            const seq = dataset.seq;
            const commentEl = document.getElementById(`comment_${seq}`);
            const targetEl = commentEl.querySelector(".comment");

            /**
            * targetEl의 하위 요소로 textarea가 있는 경우 : 댓글 수정 처리
            * 없는 경우는 -> 비회원 비밀번호 검증 또는 texarea 노출 처리
            */

            const textarea = targetEl.querySelector("textarea");
            if (textarea) { // 댓글 수정 처리
                const content = textarea.value.trim();
                const formData = new FormData();
                formData.append('mode', 'edit');
                formData.append('seq', seq);
                formData.append('content', content);

                const { ajaxLoad } = commonLib;

                ajaxLoad('PATCH', '/api/comment', formData, 'json')
                    .then(res => {
                        targetEl.innerHTML = content.replace(/\n/gm, '<br>')
                                                    .replace(/\r/gm, '');

                    })
                    .catch(err => console.error(err));

                return;
            }


            if (dataset.editable == 'false') { // 비회원 댓글 -> 비밀번호 확인 필요
                checkRequiredPassword(seq, () => callbackSuccess(seq), function() {
                    // 비번확인이 필요한 경우
                    const passwordBox = document.createElement("input");
                    passwordBox.type = "password";
                    passwordBox.placeholder = "비밀번호 입력";
                    const button = document.createElement("button");
                    const buttonText = document.createTextNode("확인");
                    button.appendChild(buttonText);

                    targetEl.innerHTML = "";
                    targetEl.appendChild(passwordBox);
                    targetEl.appendChild(button);

                    /**
                    * 비회원 비밀번호 확인 버튼 클릭시
                    *
                    */
                    button.addEventListener("click", async function() {
                        const guestPw = passwordBox.value.trim();
                        if (!guestPw) {
                            alert('비밀번호를 입력하세요.');
                            passwordBox.focus();
                            return;
                        }

                        const { ajaxLoad } = commonLib;

                        try {
                            const result = await ajaxLoad('GET', `/api/comment/auth_validate?password=${guestPw}`);

                            callbackSuccess(seq); // textarea 보여주고,

                        } catch (err) { // 비밀번호 검증 실패시
                            alert('비밀번호가 일치하지 않습니다.');
                        }
                    });
                });
            } else { // 댓글 수정 가능 권한인 경우
                callbackSuccess(seq);
            }
        });
    }

    /**
    * 인증 성공시
    *      textarea로 수정 노출
    *
    * @param seq : 댓글 등록 번호
    *           1) 댓글 가져오기
    *           2) textarea 생성 : 댓글 내용
    *
    */
    async function callbackSuccess(seq) {
        try {
            const { ajaxLoad } = commonLib;

            // 댓글 가져오기
            const result = await ajaxLoad('GET', `/api/comment/${seq}`, null, 'json');
            const data = result.data;

            const targetEl = document.querySelector(`#comment_${seq} .comment`);
            const textarea = document.createElement("textarea");
            textarea.value = data.content;

            targetEl.innerHTML = "";
            targetEl.appendChild(textarea);

        } catch (err) {
            console.error(err);
        }
    }

    /**
    * 비회원 비밀번호 필요한지 체크
    *
    * @param seq : 댓글 등록번호
    * @param success : 비밀번호 검증 이미 완료 된 상태 -> 댓글 수정 textarea 노출
    * @param failure : 비밀번호 검증 필요 -> 비밀번호 입력 항목 노출
    */
    async function checkRequiredPassword(seq, success, failure) {
        try {
            const { ajaxLoad } = commonLib;

            const result = await ajaxLoad('GET', `/api/comment/auth_check?seq=${seq}`, null, 'json');

            if (typeof success == 'function') {
                success();
            }
        } catch (err) { // 비밀번호 검증 필요
            if (typeof failure == 'function') {
                failure();
            }
        }
    }

    /*
    const editComments = document.getElementsByClassName("edit_comment");
    const { ajaxLoad } = commonLib;
    for (const el of editComments) {
        el.addEventListener("click", function() {
            const seq = this.dataset.seq;
            const targetEl = document.querySelector(`#comment_${seq} .comment`);
            const textAreaEl = targetEl.querySelector("textarea");
            if (textAreaEl) { // 댓글 수정 처리
                if (!confirm('정말 수정하겠습니까?')) {
                    return;
                }

                let content = textAreaEl.value;

                const formData = new FormData();
                formData.append('seq', seq);
                formData.append('content', content);

                ajaxLoad('PATCH', `/api/comment`, formData, 'json')
                    .then(res => {
                        if (res.success) {
                            content = content.replace(/\n/gm, '<br>')
                                            .replace(/\r/gm, '');
                            targetEl.innerHTML = content;
                        } else { // 댓글 수정 실패시
                            alert(res.message);
                        }
                    })
                    .catch(err => console.error(err));

            } else { // TextArea 생성
                const textArea = document.createElement("textarea");

                ajaxLoad('GET', `/api/comment/${seq}`, null, 'json')
                    .then(res => {
                        if (res.success && res.data) {
                            const data = res.data;
                            targetEl.innerHTML = "";
                            if (!data.member && !data.editable) {
                                // 비회원 비밀번호 확인 필요
                                const passwordBox = document.createElement("input");
                                passwordBox.type = "password";
                                passwordBox.placeholder = "비밀번호 입력";

                                const button = document.createElement("button");
                                button.type = 'button';

                                const buttonTxt = document.createTextNode("확인");
                                button.appendChild(buttonTxt);




                                button.addEventListener("click", function() {

                                   const guestPw = passwordBox.value.trim();
                                   if (!guestPw) {
                                        alert("비밀번호를 입력하세요.");
                                        passwordBox.focus();
                                        return;
                                   }

                                    ajaxLoad("GET", `/api/comment/auth_check?seq=${seq}&guestPw=${guestPw}`, null, 'json')
                                        .then(res => {
                                            // 비회원 비밀번호 검증 완료 후
                                            // textarea 노출

                                        })
                                        .catch(err => { // 비회원 비밀번호 검증 필요 : 비번 확인 항목 노출
                                            targetEl.appendChild(passwordBox);
                                            targetEl.appendChild(button);
                                        });
                                });

                            } else {
                                textArea.value = data.content;
                                targetEl.appendChild(textArea);
                            }

                        }
                    })
                    .catch(err => console.error(err));

                /*
                fetch(`/api/comment/${seq}`)
                    .then(res => {
                        const data = res.json();
                        console.log(data);
                        console.log(data.data);
                    })
                    .catch(err => console.error(err));
                    */ /*
            }

        });
    }
    */
    /* 댓글 수정 버튼 클릭 처리 E */
});