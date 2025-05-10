let display = document.getElementById("display");
let current = "";

function input(val) {
  current += val;
  display.textContent = current;
}

function clearDisplay() {
  current = "";
  display.textContent = "0";
}

function calculate() {
  try {
    current = eval(current).toString();
    display.textContent = current;
  } catch {
    display.textContent = "Erro";
    current = "";
  }
}
