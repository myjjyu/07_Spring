const axiosHelper = {
  ajax: async function (url, methood, formData, headers={}, isMultipart=false) {
    let response = null;

    if(isMultipart) {
      headers["Content-Type"]= "multipart/form-data";
    }


    try {
      // 연산자에 따라 GET,POST,PUT,DELETE 선택
      // 전송방식에 따라 axios에서 파라미터를 처리하는 방법이 다름
      switch (methood.toLowerCase()) {
        case "get":
        let data = null;
        try {
          formData = Object.fromEntries(formData);
        } catch (e) {
          data = formData;
        }

          response = await axios.get(url, {
            // GET 방식으로 전송할 때는 params 속성을 사용하여 JSON 객체로 구성해야 함
            params: data,
            headers: headers
          });
          break;
        case "post":
          response = await axios.post(url, formData, {
            headers: headers
          });
          break;
        case "put":
          response = await axios.put(url, formData, {
            headers: headers
          });
          break;
        case "delete":
          response = await axios.delete(url, {
            // DELETE 방식으로 전송할 때는 data 속성을 사용하여 FormData 객체를 그대로 전달해야 함
            data: formData,
            headers: headers
          });
          break;
      }
    } catch (error) {
      let alertTitle = null;
      let alertMsg = null;
      console.log(error);

      // SpringBoot로부터 전달받은 에러 메시지가 있다면?
      if (error.response?.data) {
        const data = error.response.data;


        alertTitle =`${data.status} Error`;
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
      // alert(alertMsg);
      await utilHelper.alertDanger(alertTitle, alertMsg);
    }
    return response?.data;
  },
  get: async function (url, formData, headers={}, isMultipart=false) {
    return await this.ajax(url, "get", formData, headers, isMultipart);
  },

  post: async function (url, formData, headers={}, isMultipart=false) {
    return await this.ajax(url, "post", formData, headers, isMultipart);
  },

  put: async function (url, formData, headers={}, isMultipart=false) {
    return await this.ajax(url, "put", formData, headers, isMultipart);
  },

  delete: async function (url, formData, headers={}, isMultipart=false) {
    return await this.ajax(url, "delete", formData, headers, isMultipart);
  },

  getMultipart: async function (url, formData, headers={}) {
    return await this.get(url, formData, headers, true);
  },

  postMultipart: async function (url, formData, headers={}) {
    return await this.post(url, formData, headers, true);
  },

  putMultipart: async function (url, formData, headers={}) {
    return await this.put(url, formData, headers, true);
  },

  deleteMultipart: async function (url, formData, headers={}) {
    return await this.delete(url, formData, headers, true);
  }
}
