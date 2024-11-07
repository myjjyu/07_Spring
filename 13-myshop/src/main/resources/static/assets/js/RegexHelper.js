class StringFormatException extends Error {
  #selector;
//StringFormatException 클래스를 Error 클래스로부터 상속받아 정의합니다.
//#selector는 private 필드로, 이 클래스에서만 접근 가능한 selector를 저장합니다.

  constructor(msg = "잘못된 요청 입니다.", selector = undefined) {
    super(msg);
    super.name = "StringFormatException";
    this.#selector = selector;
  }
//생성자 함수입니다. msg와 selector를 인수로 받아서 StringFormatException 객체를 초기화합니다. 
//msg는 예외 메시지, selector는 선택자를 저장합니다. s
//uper(msg)로 부모 클래스의 생성자를 호출하고, super.name을 설정하여 예외의 이름을 정의합니다.
  

get selector() {
    return this.#selector; 
  } //selector에 대한 getter 메소드입니다. #selector 값을 반환합니다.

  get element() {
    const el = this.#selector !== null ? document.querySelector(this.#selector) : undefined;
    return el;
  }
}//element는 #selector가 null이 아닌 경우 해당 선택자를 사용하여 DOM 요소를 찾습니다. 요소가 없으면 undefined를 반환합니다.

class RegexHelper { //RegexHelper 클래스를 정의합니다.
  value(selector, msg) {
    const content = document.querySelector(selector).value;
//value 메소드입니다. selector로 선택된 요소의 값을 가져옵니다.

    if (
      content === undefined ||
      content === null ||
      (typeof content === "string" && content.trim().length === 0)
    ) {
      throw new StringFormatException(msg, selector); 
    } //content가 undefined, null이거나 빈 문자열인 경우 StringFormatException을 던집니다.

    return true;
  }//값이 유효할 경우 true를 반환합니다.

  maxLength(selector, len, msg) {
    this.value(selector, msg);

    const content = document.querySelector(selector).value;

    if (content.trim().length > len) {
      throw new StringFormatException(msg, selector);
    }

    return true;
  }

  minLength(selector, len, msg) {
    this.value(selector, msg);

    let content = document.querySelector(selector).value;

    if (content.trim().length < len) {
      throw new StringFormatException(msg, selector);
    }

    return true;
  }

  compareTo(origin, compare, msg) {
    this.value(origin, msg);
    this.value(compare, msg);

    var src = document.querySelector(origin).value.trim();
    var dsc = document.querySelector(compare).value.trim();

    if (src !== dsc) {
      throw new StringFormatException(msg, origin);
    }

    return true;
  }

  check(selector, msg) {
    const elList = document.querySelectorAll(selector);
    const checkedItem = Array.from(elList).filter((v, i) => v.checked);

    if (checkedItem.length === 0) {
      throw new StringFormatException(msg, selector);
    }
  }

  checkmin(selector, len, msg) {
    const elList = document.querySelectorAll(selector);
    const checkedItem = Array.from(elList).filter((v, i) => v.checked);

    if (checkedItem.length < len) {
      throw new StringFormatException(msg, selector);
    }
  }

  checkmax(selector, len, msg) {
    const elList = document.querySelectorAll(selector);
    const checkedItem = Array.from(elList).filter((v, i) => v.checked);

    if (checkedItem.length > len) {
      throw new StringFormatException(msg, selector);
    }
  }

  selector(selector, msg, regexExpr) {
    this.value(selector, msg);

    if (!regexExpr.test(document.querySelector(selector).value.trim())) {
      throw new StringFormatException(msg, selector);
    }

    return true;
  }

  num(selector, msg) {
    return this.selector(selector, msg, /^[0-9]*$/);
  }

  eng(selector, msg) {
    return this.selector(selector, msg, /^[a-zA-Z]*$/);
  }

  kor(selector, msg) {
    return this.selector(selector, msg, /^[ㄱ-ㅎ가-힣]*$/);
  }

  engNum(selector, msg) {
    return this.selector(selector, msg, /^[a-zA-Z0-9]*$/);
  }

  korNum(selector, msg) {
    return this.selector(selector, msg, /^[ㄱ-ㅎ가-힣0-9]*$/);
  }

  email(selector, msg) {
    return this.selector(
      selector,
      msg,
      /^([\w-]+(?:\.[\w-]+)*)@((?:[\w-]+\.)*\w[\w-]{0,66})\.([a-z]{2,6}(?:\.[a-z]{2})?)$/i
    );
  }

  cellphone(selector, msg) {
    return this.selector(selector, msg, /^01(?:0|1|[6-9])(?:\d{3}|\d{4})\d{4}$/);
  }

  telphone(selector, msg) {
    return this.selector(selector, msg, /^\d{2,3}\d{3,4}\d{4}$/);
  }

  phone(selector, msg) {
    this.value(selector, msg);
    const content = document.querySelector(selector).value.trim();
    var check1 = /^01(?:0|1|[6-9])(?:\d{3}|\d{4})\d{4}$/;
    var check2 = /^\d{2,3}\d{3,4}\d{4}$/;

    if (!check1.test(content) && !check2.test(content)) {
      throw new StringFormatException(msg, selector);
    }
    return true;
  }
}

const regexHelper = new RegexHelper();




