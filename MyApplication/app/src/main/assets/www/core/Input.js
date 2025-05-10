class Input {

  static DOWN = 0;
  static UP = 1;
  static MOVE = 2;
  static WHEEL = 3;
  static PRESS = 4;

  static LEFT = 0;
  static MIDDLE = 1;
  static RIGHT = 2;



  static mouseX = 0;
  static mouseY = 0;
  static mouseDX = 0;
  static mouseDY = 0;

  static prevX = 0;
  static prevY = 0;


  static MAX_KEYBOARD_KEYS = 256;
  static previousKeyState = [Input.MAX_KEYBOARD_KEYS];
  static currentKeyState = [Input.MAX_KEYBOARD_KEYS];

  static MAX_TOUCH_POINTS = 10;

  static currentButtonState = [Input.MAX_TOUCH_POINTS];
  static previousButtonState = [Input.MAX_TOUCH_POINTS];



  static init() {
    for (let i = 0; i < Input.MAX_TOUCH_POINTS; i++) {
      Input.currentButtonState[i] = 0;
      Input.previousButtonState[i] = 0;
    }
    for (let i = 0; i < Input.MAX_KEYBOARD_KEYS; i++) {
      Input.currentKeyState[i] = 0;
      Input.previousKeyState[i] = 0;
    }
  }

  static onKeyDown(code) {


  }

  static onKeyUp(code) {

  }

  static onMouseWheel(delta) {

  }

  static onMouseDown(button) {
 
    Input.currentButtonState[button] = 1;
  }

  static onMouseUp(button) {
   
    Input.currentButtonState[button] = 0;
  }

  static onMouseMove(x, y) {
    Input.mouseX = x;
    Input.mouseY = y;
    Input.mouseDX = x - Input.prevX;
    Input.mouseDY = y - Input.prevY;

    Input.mouseX = x;
    Input.mouseY = y;

    Input.prevX = x;
    Input.prevY = y;
  }

  static isKeyDown(code) {

  }

  static isMouseDown(button) {
    return Input.mouseButtons.has(button);
  }

  static getMousePosition() {
    return { x: Input.mouseX, y: Input.mouseY };
  }
  static isMouseDown(button) {
    let downd = false;
    if ((Input.currentButtonState[button] === 1) && (Input.currentButtonState[button] === 1)) downd = true;
    return downd;
  }

  static isMousePressed(button) {
    let pressed = false;
    if ((Input.currentButtonState[button] === 1) && (Input.previousButtonState[button]) === 0) pressed = true;
    return pressed;
  }

  static isMouseReleased(button) {
    let released = false;

    if ((Input.currentButtonState[button] === 0) && (Input.previousButtonState[button] === 1)) released = true;

    return released;

  }

  static isMouseUp(button) {
    let up = false;

    if (Input.currentButtonState[button] === 0) up = true;

    return up;
  }

  static isKeyDown(key) {
    return false;
  }

  static isKeyPressed(key) {
    return false;
  }

  static isKeyReleased(key) {
    return false;
  }


  static update() {
    Input.mouseDX = 0;
    Input.mouseDY = 0;

    for (let i = 0; i < Input.MAX_TOUCH_POINTS; i++) {
      Input.previousButtonState[i] = Input.currentButtonState[i];
    }
    for (let i = 0; i < Input.MAX_KEYBOARD_KEYS; i++) {
      Input.previousKeyState[i] = Input.currentKeyState[i];
    }
  }
}
