window.addEventListener("DOMContentLoaded", function() {
    // 댓글 작성 후 URL에 comment_id=댓글 등록번호 -> hash 추가 -> 이동
    if (location.search.indexOf("comment_id=") != -1) {
        const searchParams = new URLSearchParams(location.search);

        const seq = searchParams.get("comment_id");
        //searchParams.delete("comment_id");

        //location.search = searchParams.toString();

        location.hash=`#comment_${seq}`;
    }

    /* 댓글 수정 버튼 클릭 처리 S */
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

                                targetEl.appendChild(passwordBox);
                                targetEl.appendChild(button);

                                const guestPw = passwordBox.value.trim();
                                if (!guestPw) {
                                    alert("비밀번호를 입력하세요.");
                                    passwordBox.focus();
                                    return;
                                }

                                button.addEventListener("click", function() {

                                    ajaxLoad("GET", `/api/comment/auth_check?seq=${seq}&guestPw=${guestPw}`, null, 'json')
                                        .then(res => {
                                            console.log("여기...");
                                           //targetEl.innerHTML = "";
                                           //textArea.value = data.content;
                                           //targetEl.appendChild(textArea);
                                           el.click();
                                        })
                                        .catch(err => console.error(err));
                                });
                            }

                            textArea.value = data.content;
                            targetEl.appendChild(textArea);

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
                    */
            }

        });
    }
    /* 댓글 수정 버튼 클릭 처리 E */
});