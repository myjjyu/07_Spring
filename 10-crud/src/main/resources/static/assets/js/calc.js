document.querySelector("#calcform").addEventListener("submit", async (e) => {
    e.preventDefault();
    const form = e.currentTarget;

    // 백엔드 주소
    const backend = e.currentTarget.getAttribute("action");

    // 응답결과가 저장될 객체
    let response = null;

    // 선택된 연산자만 추출
    const op = form.querySelector("select[name=op]").value;

    // 가상의 Form 데이터를 생성하여 Axios로 전송
    const formData = new FormData(form);

    try {
        // 연산자에 따라 GET,POST,PUT,DELETE 선택
        // 전송방식에 따라 axios에서 파라미터를 처리하는 방법이 다름
        switch (op) {
            case "+":
                response = await axios.get(backend, {
                    // GET 방식으로 전송할 때는 params 속성을 사용하여 JSON 객체로 구성해야 함
                    params: Object.fromEntries(formData)
                });
                break;
            case "-":
                response = await axios.post(backend, formData);
                break;
            case "x":
                response = await axios.put(backend, formData);
                break;
            case "/":
                response = await axios.delete(backend, {
                    // DELETE 방식으로 전송할 때는 data 속성을 사용하여 FormData 객체를 그대로 전달해야 함
                    data: formData
                });
                break;
        }
    } catch (error) {
        let alertMsg = null;
        console.log(error);

        // SpringBoot로부터 전달받은 에러 메시지가 있다면?
        if (error.response?.data) {
            const data = error.response.data;

            // 메시지 창에 표시할 내용
            alertMsg = data.message;

            // 백엔드에서 발생한 상세 에러 내용을 브라우저 콘솔에 출력
            // --> 이 코드는 보안에 취약할 수 있으므로 실제 서비스에서는 제거할 것
            console.error("Error occurred in the backend server.");
            console.error(`[${data.status}] ${data.error}`);
            console.error(data.trace);
        } else {
            // SpringBoot로부터 전달받은 에러 메시지가 없다면?
            // --> Axios의 에러 메시지를 그대로 사용

            // 메시지 창에 표시할 내용
            alertMsg = error.message;

            // Axios의 기본 에러 메시지를 추출하여 문자열로 구성
            console.error("Error occurred in the Axios.");
            console.error(`[${error.code}] ${error.message}`);
        }

        // 메시지박스로 에러 내용 표시
        alert(alertMsg);

        // catch에서 return을 하더라도 finally가 수행된 후 실행이 취소된다.
        return;
    }

    // 결과처리
    const result = response.data;

    // 결과를 화면에 출력
    // json 결과에 포함된 x, y, result를 사용하여 연산식 문자열을 만든 후, li태그로 추가
    const li = document.createElement("li");
    li.textContent = `${result.x} ${op} ${result.y} = ${result.result}`;
    document.querySelector("#result").appendChild(li);
});