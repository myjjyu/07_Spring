const axiosHelper = {
  ajax: async function (url, methood, formData) {
    let response = null;

    try {
      // 연산자에 따라 GET,POST,PUT,DELETE 선택
      // 전송방식에 따라 axios에서 파라미터를 처리하는 방법이 다름
      switch (methood.toLowerCase()) {
        case "get":
          response = await axios.get(url, {
            // GET 방식으로 전송할 때는 params 속성을 사용하여 JSON 객체로 구성해야 함
            params: formData && Object.fromEntries(formData),
          });
          break;
        case "post":
          response = await axios.post(url, formData);
          break;
        case "put":
          response = await axios.put(url, formData);
          break;
        case "delete":
          response = await axios.delete(url, {
            // DELETE 방식으로 전송할 때는 data 속성을 사용하여 FormData 객체를 그대로 전달해야 함
            data: formData,
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
    }
    return response?.data;
  },
  get: async function (url, formData) {
    return await this.ajax(url, "get", formData);
  },

  post: async function (url, formData) {
    return await this.ajax(url, "post", formData);
  },

  put: async function (url, formData) {
    return await this.ajax(url, "put", formData);
  },

  delete: async function (url, formData) {
    return await this.ajax(url, "delete", formData);
  }
}
