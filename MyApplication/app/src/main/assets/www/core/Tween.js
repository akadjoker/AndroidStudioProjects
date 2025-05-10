

class Tween
{
    constructor(target, property, from, to, duration, easing = Tween.EASE_LINEAR, mode = Tween.MODE_ONCE, now=true, autoremove = true) {
      this.target = target;
      this.property = property;
      this.from = from;
      this.to = to;
      this.value = 0;
      this.duration = duration;
      this.easing = easing;
      this.mode = mode;
      this.autoremove = autoremove;
      this.OnEnded = null;
      this.elapsed = 0;
      this.done = false;
      this.forward = true;
      this.active = now;
      this.playing = false;
      this.finished = false;
    }
  
    update(dt) {
      if (!this.active || this.done) return;
  
        this.elapsed += dt;
 
          let t = Math.min(this.elapsed / this.duration, 1);
          t = Tween.evaluateEasing(this.easing, t);
  
        const start = this.forward ? this.from : this.to;
        const end = this.forward ? this.to : this.from;
        this.value = start + (end - start) * t;
        this.target[this.property] =  this.value;
  
        if (this.elapsed >= this.duration)
        {
          switch (this.mode)
          {
          case Tween.MODE_ONCE:
            this.done = true;
              this.target[this.property] = this.to;
              this.playing = false;
              this.finished = true;
            break;
          case Tween.MODE_LOOP:
                this.reset();
                if (this.OnEnded) this.OnEnded();
                this.playing = true;
                this.finished = true;
                break;
            case Tween.MODE_PINGPONG:
                this.reset();
                if (this.OnEnded) this.OnEnded();
                this.forward = !this.forward;
                this.playing = true;
                this.finished = false;
                break;
          case Tween.MODE_PERSIST:
            if (this.OnEnded) this.OnEnded();
              this.done = true;
              this.playing = false;
              this.finished = true;
            break;
        }
      }
    }
  
    reset() {
      this.elapsed = 0;
      this.done = false;
      this.active = true;
      this.playing = false;
      this.finished = false;
    }
  
  play() {
      this.elapsed = 0;
      this.active = true;
      this.done = false;
      this.playing = true;
    }
  
    pause() {
      this.active = false;
    }
  }
  
  // Easing types
// Tween easing types
Tween.LINEAR              = 0;

Tween.EASE_IN_SINE        = 1;
Tween.EASE_OUT_SINE       = 2;
Tween.EASE_IN_OUT_SINE    = 3;

Tween.EASE_IN_QUAD        = 4;
Tween.EASE_OUT_QUAD       = 5;
Tween.EASE_IN_OUT_QUAD    = 6;

Tween.EASE_IN_CUBIC       = 7;
Tween.EASE_OUT_CUBIC      = 8;
Tween.EASE_IN_OUT_CUBIC   = 9;

Tween.EASE_IN_QUART       = 10;
Tween.EASE_OUT_QUART      = 11;
Tween.EASE_IN_OUT_QUART   = 12;

Tween.EASE_IN_QUINT       = 13;
Tween.EASE_OUT_QUINT      = 14;
Tween.EASE_IN_OUT_QUINT   = 15;

Tween.EASE_IN_EXPO        = 16;
Tween.EASE_OUT_EXPO       = 17;
Tween.EASE_IN_OUT_EXPO    = 18;

Tween.EASE_IN_CIRC        = 19;
Tween.EASE_OUT_CIRC       = 20;
Tween.EASE_IN_OUT_CIRC    = 21;

Tween.EASE_IN_BACK        = 22;
Tween.EASE_OUT_BACK       = 23;
Tween.EASE_IN_OUT_BACK    = 24;

Tween.EASE_IN_ELASTIC     = 25;
Tween.EASE_OUT_ELASTIC    = 26;
Tween.EASE_IN_OUT_ELASTIC = 27;

Tween.EASE_IN_BOUNCE      = 28;
Tween.EASE_OUT_BOUNCE     = 29;
Tween.EASE_IN_OUT_BOUNCE  = 30;

  
  // Modes
  Tween.MODE_ONCE = 0;
  Tween.MODE_LOOP = 1;
  Tween.MODE_PINGPONG = 2;
  Tween.MODE_PERSIST = 3;
  
  // Easing evaluator
Tween.evaluateEasing = function (type, t)
{
  const PI = Math.PI;
  const c1 = 1.70158;
  const c2 = c1 * 1.525;
  const c3 = c1 + 1;
  const c4 = (2 * PI) / 3;
  const c5 = (2 * PI) / 4.5;

  function bounceOut(x)
  {
      const n1 = 7.5625;
      const d1 = 2.75;
      if (x < 1 / d1) return n1 * x * x;
      if (x < 2 / d1) return n1 * (x -= 1.5 / d1) * x + 0.75;
      if (x < 2.5 / d1) return n1 * (x -= 2.25 / d1) * x + 0.9375;
      return n1 * (x -= 2.625 / d1) * x + 0.984375;
  }

  Tween.evaluateEasing = function (type, t)
  {
    switch (type)
    {
        // Linear
        case Tween.LINEAR:
            return t;

        // Sine
        case Tween.EASE_IN_SINE:
            return 1 - Math.cos((t * PI) / 2);
        case Tween.EASE_OUT_SINE:
            return Math.sin((t * PI) / 2);
        case Tween.EASE_IN_OUT_SINE:
            return -(Math.cos(PI * t) - 1) / 2;

        // Quad
        case Tween.EASE_IN_QUAD:
            return t * t;
        case Tween.EASE_OUT_QUAD:
            return 1 - (1 - t) * (1 - t);
        case Tween.EASE_IN_OUT_QUAD:
            return t < 0.5 ? 2 * t * t : 1 - Math.pow(-2 * t + 2, 2) / 2;

        // Cubic
        case Tween.EASE_IN_CUBIC:
            return t * t * t;
        case Tween.EASE_OUT_CUBIC:
            return 1 - Math.pow(1 - t, 3);
        case Tween.EASE_IN_OUT_CUBIC:
            return t < 0.5 ? 4 * t * t * t : 1 - Math.pow(-2 * t + 2, 3) / 2;

        // Quart
        case Tween.EASE_IN_QUART:
            return t * t * t * t;
        case Tween.EASE_OUT_QUART:
            return 1 - Math.pow(1 - t, 4);
        case Tween.EASE_IN_OUT_QUART:
            return t < 0.5 ? 8 * t * t * t * t : 1 - Math.pow(-2 * t + 2, 4) / 2;

        // Quint
        case Tween.EASE_IN_QUINT:
            return t * t * t * t * t;
        case Tween.EASE_OUT_QUINT:
            return 1 - Math.pow(1 - t, 5);
        case Tween.EASE_IN_OUT_QUINT:
            return t < 0.5 ? 16 * t * t * t * t * t : 1 - Math.pow(-2 * t + 2, 5) / 2;

        // Expo
        case Tween.EASE_IN_EXPO:
            return t === 0 ? 0 : Math.pow(2, 10 * t - 10);
        case Tween.EASE_OUT_EXPO:
            return t === 1 ? 1 : 1 - Math.pow(2, -10 * t);
        case Tween.EASE_IN_OUT_EXPO:
            if (t === 0) return 0;
            if (t === 1) return 1;
            return t < 0.5
                ? Math.pow(2, 20 * t - 10) / 2
                : (2 - Math.pow(2, -20 * t + 10)) / 2;

        // Circ
        case Tween.EASE_IN_CIRC:
            return 1 - Math.sqrt(1 - Math.pow(t, 2));
        case Tween.EASE_OUT_CIRC:
            return Math.sqrt(1 - Math.pow(t - 1, 2));
        case Tween.EASE_IN_OUT_CIRC:
            return t < 0.5
                ? (1 - Math.sqrt(1 - Math.pow(2 * t, 2))) / 2
                : (Math.sqrt(1 - Math.pow(-2 * t + 2, 2)) + 1) / 2;

        // Back
        case Tween.EASE_IN_BACK:
            return c3 * t * t * t - c1 * t * t;
        case Tween.EASE_OUT_BACK:
            return 1 + c3 * Math.pow(t - 1, 3) + c1 * Math.pow(t - 1, 2);
        case Tween.EASE_IN_OUT_BACK:
            return t < 0.5
                ? (Math.pow(2 * t, 2) * ((c2 + 1) * 2 * t - c2)) / 2
                : (Math.pow(2 * t - 2, 2) * ((c2 + 1) * (2 * t - 2) + c2) + 2) / 2;

        // Elastic
        case Tween.EASE_IN_ELASTIC:
            return t === 0
                ? 0
                : t === 1
                ? 1
                : -Math.pow(2, 10 * t - 10) * Math.sin((t * 10 - 10.75) * c4);
        case Tween.EASE_OUT_ELASTIC:
            return t === 0
                ? 0
                : t === 1
                ? 1
                : Math.pow(2, -10 * t) * Math.sin((t * 10 - 0.75) * c4) + 1;
        case Tween.EASE_IN_OUT_ELASTIC:
            return t === 0
                ? 0
                : t === 1
                ? 1
                : t < 0.5
                ? -(Math.pow(2, 20 * t - 10) * Math.sin((20 * t - 11.125) * c5)) / 2
                : (Math.pow(2, -20 * t + 10) * Math.sin((20 * t - 11.125) * c5)) / 2 + 1;

        // Bounce
        case Tween.EASE_IN_BOUNCE:
            return 1 - bounceOut(1 - t);
        case Tween.EASE_OUT_BOUNCE:
            return bounceOut(t);
        case Tween.EASE_IN_OUT_BOUNCE:
            return t < 0.5
                ? (1 - bounceOut(1 - 2 * t)) / 2
                : (1 + bounceOut(2 * t - 1)) / 2;

        // Default fallback (linear)
        default:
            return t;
    }
}
};
