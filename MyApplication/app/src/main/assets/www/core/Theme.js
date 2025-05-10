
const BACKGROUND = 0;
const LABEL = 1;
const BUTTON = 2;
const BUTTON_HOVER = 3;
const BUTTON_PRESS = 4;

const PANEL = 5
const PANEL_BORDERLIGHT = 6;
const PANEL_BORDERDARK = 7;


const TOGGLE = 8;
const TOGGLE_BACKGROUND = 9;
const TOGGLE_BACKGROUND_CHECKED = 10;
const TOGGLE_TEXT = 11;
const TOGGLE_KNOB = 12;
const TOGGLE_HOVER_OUTLINE = 13;

const CHECKBOX = 14;
const CHECKBOX_HOVER = 15;
const CHECKBOX_CHECKED = 16;
const CHECKBOX_BORDER = 17;
const CHECKBOX_CHECKMARK = 18;
const CHECKBOX_TEXT = 19;
const CHECKBOX_OUTLINE = 20;

const RADIO_OUTER = 21;
const RADIO_INNER = 22;
const RADIO_TEXT = 23;
const RADIO_OUTLINE = 24;

const RADIO_GROUP = 25;
const RADIO_GROUP_BORDER = 25;
const RADIO_GROUP_TEXT = 26;
const RADIO_GROUP_OUTLINE = 27;

    
 

class Theme 
{
    static colors = [];
   static setDefault()
{
        Theme.colors = [];
        Theme.colors[LABEL] = "#ffffff";
        Theme.colors[BACKGROUND] = "#000";
        Theme.colors[BUTTON] = "rgb(192,192,192)";
        Theme.colors[BUTTON_HOVER] =  "rgb(210,210,210)";
        Theme.colors[BUTTON_PRESS] = "rgb(128,128,128)";
       
       Theme.colors[PANEL] = "rgb(160, 150, 150)";
       Theme.colors[PANEL_BORDERLIGHT] = "#ffffff";
       Theme.colors[PANEL_BORDERDARK] = "#888888";

       Theme.colors[TOGGLE_BACKGROUND] = "#222";
       Theme.colors[TOGGLE_BACKGROUND_CHECKED] = "#777";
       Theme.colors[TOGGLE_TEXT] = "#fff";
       Theme.colors[TOGGLE_KNOB] = "#fff";
       Theme.colors[TOGGLE_HOVER_OUTLINE] = "rgba(54, 49, 49, 0.4)";

       Theme.colors[CHECKBOX] = "#ccc";
       Theme.colors[CHECKBOX_HOVER] = "#aaa";
       Theme.colors[CHECKBOX_CHECKED] = "#000";
       Theme.colors[CHECKBOX_BORDER] = "#444";
       Theme.colors[CHECKBOX_CHECKMARK] = "#000";
       Theme.colors[CHECKBOX_TEXT] = "#000";
       Theme.colors[CHECKBOX_OUTLINE] = "rgba(0,0,0,0.4)";

       Theme.colors[RADIO_OUTER] = "#555";
       Theme.colors[RADIO_INNER] = "#444";
       Theme.colors[RADIO_TEXT] = "#000";
       Theme.colors[RADIO_OUTLINE] = "rgba(0,0,0,0.4)";

       Theme.colors[RADIO_GROUP] = "#ccc";
       Theme.colors[RADIO_GROUP_BORDER] = "#000";
       Theme.colors[RADIO_GROUP_TEXT] = "#000";
       Theme.colors[RADIO_GROUP_OUTLINE] = "rgba(40,40,40,0.6)";
  
 
 
       
    }
}

//  // Buttons
 
//  // ComboBox
//  static comboBackground = "#eee";
//  static comboText = "#000";
//  static comboArrow = "#000";
//  static comboBorder = "#aaa";
//  static comboListBackground = "#ccc";
//  static comboListHover = "rgba(100,100,100,0.1)";
//  static comboListSelect = "rgba(100,100,100,0.3)";
//  static comboSelectedDragging = "rgba(100,100,100,0.5)";

//  // ListBox
//  static listBackground = "#ccc";
//  static listHover = "rgba(65, 64, 64, 0.2)";
//  static listSelected = "rgba(100,100,100,0.3)";
//  static listSelectedDragging = "rgba(100,100,100,0.5)";
//  static listText = "#000";
//  static listBorder = "#999";
 

//  // Knob
//  static knobBackground = "#777";
//  static knobPointer = "#aaa";
//  static knobText = "#000";
//  static knobHoverOutline = "rgba(40,40,40,0.8)";
//  static knobMarkers = "#ccc";
 

//  // ProgressBar
//  static progressBackground = "#888";
//  static progressFill = "#444";
//  static progressBorder = "#000";
//  static progressText = "#fff";
 

//  // Slider
//  static sliderTrack = "#988";
//  static sliderFill = "#555";
//  static sliderThumb = "#222";
//  static sliderBorder = "#aaa";
//  static sliderText = "#000";
 

//  // RadioButton
//  static radioOuter = "#555";
//  static radioInner = "#444";
//  static radioText = "#000";
//  static radioHoverOutline = "rgba(0,0,0,0.9)";

//  //RadioGroup
//  static radioGroupBackground = "#ccc";
//  static radioGroupBorder = "#000";
//  static radioGroupText = "#000";
//  static radioGroupHoverOutline = "rgba(40,40,40,0.6)";
 

//  // Checkbox
//  static checkboxBackground = "#ccc";
//  static checkboxHover = "#aaa";
//  static checkboxBorder = "#444";
//  static checkboxMark = "#000";
//  static checkboxText = "#000";
//  static checkboxOutline = "rgba(0,0,0,0.4)";
 

//  // ValueField
//  static dragFieldBackground = "#eee";
//  static dragFieldBorder = "#000";
//  static dragFieldText = "#000";
//  static dragFieldLabel = "#000";
//  static dragFieldHoverOutline = "rgba(0,0,0,0.8)";

//  // ToggleSwitch
//  static toggleBackground = "#aaa";
//  static toggleBackgroundChecked = "#4caf50";
//  static toggleText = "#fff";
//  static toggleKnob = "#fff";
//  static toggleHoverOutline = "rgba(0,0,0,0.4)";

//  // Tabs
//  static tabBackground = "#ccc";
//  static tabActive = "#ddd";
//  static tabInactive = "#aaa";
//  static tabLabel = "#000";
//  static tabHoverOutline = "#999";
//  static tabCloseIcon = "#000";
//  static tabScrollButton = "#888";
//  static tabScrollIcon = "#fff";

//  // Window (janela)
//  static windowBar         = "#333";
//  static windowBarHover    = "#444";
//  static windowTitle       = "#fff";
//  static windowBackground  = "#f0f0f0";
//  static windowResizeLines = "#888";

//  static windowButtonClose     = "#aaa";
//  static windowButtonMinimize  = "#aaa";
//  static windowButtonSymbol    = "#000";
//  static windowButtonSymbolHover = "#f00";

//  static windowBorder          = "#000";

//  static scrollbarTrack = "rgba(255,255,255,0.1)";
//  static scrollbarThumb = "rgba(255,255,255,0.4)";

//  ///stepper
//  static stepperBackground = "#777";
//  static stepperBorder = "#bdc3c7";
//  static stepperButton = "#dfe6e9";
//  static stepperButtonText = "#2d3436";
//  static stepperText = "#000";

//  //spinner
//  static spinnerTrack = "#ccc";
//  static spinnerHead = "#3498db";

//  static toastBackground = "#333";
//  static toastBorder = "#555";
//  static toastText = "#fff";


//  static imageBorder = "#888";


//  static datepickerBackground = "#ecf0f1";
//  static datepickerBorder = "#bdc3c7";
//  static datepickerHeader = "#2c3e50";
//  static datepickerWeekday = "#7f8c8d";
//  static datepickerSelected = "#3498db";
//  static datepickerText = "#2c3e50";



//  // Shared
//  static highlight = "#4caf50";
//  static textDefault = "#000";
//  static background = "#f0f0f0";