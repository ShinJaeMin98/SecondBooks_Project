window.addEventListener("DOMContentLoaded", function() {
    /* 양식 공통 처리 S */
    const formActions = document.getElementsByClassName("form_action");
    for (const el of formActions) {
        el.addEventListener("click", function() {
            const dataset = this.dataset;
            const mode = dataset.mode || "edit";
            const formName = dataset.formName;
            if (!formName || !document[formName]) {
                return;
            }

            const formEl = document[formName];
           formEl._method.value = mode == 'delete' ? 'DELETE' : 'PATCH';

           const modeTitle = mode == 'delete' ? '삭제' : '수정';

           const chks = formEl.querySelectorAll("input[name='chk']:checked");

           if (chks.length == 0) { // 체크가 안된 경우
                alert(`${modeTitle}할 항목을 선택하세요.`);
                return;
           }

            if (confirm(`정말 ${modeTitle} 하겠습니까?`)) {
                formEl.submit();
            }

        });
    }
    /* 양식 공통 처리 E */

    /* 전체 선택 토글 기능 S */
    const checkAlls = document.getElementsByClassName("checkall");
    for (const el of checkAlls) {
        el.addEventListener("click", function() {
            const targetName = this.dataset.targetName;
            const chks = document.getElementsByName(targetName);
            for (const el of chks) {
                el.checked = this.checked;
            }
        });
    }
    /* 전체 선택 토글 기능 E */
});