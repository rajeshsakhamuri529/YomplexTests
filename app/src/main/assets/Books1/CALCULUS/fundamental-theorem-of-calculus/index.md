## Introduction

The **Fundamental Theorem of Calculus**, often referred to as the **FTC**, allows us to evaluate the *derivative of a definite integral* (**FTC-1**), or the *definite integral of a derivative* (**FTC-2**).

## Statement of FTC

**FTC-1:**
For a continuous function $f,$ defined on a closed interval $[a, b],$ we have:
$$
\frac{d}{dx}\left( \int_{a}^{x} f(t)\, dt \right) = f(x)
$$

**FTC-2:**
Suppose that a function $f$ has a continuous derivative on the interval $[a, b],$ then we have:
$$
\int_{a}^{b} f\, '(x)\, dx = f(b) - f(a)
$$
where $f\, '(x)$ represents the derivative of $f$ with respect to $x.$


### Note 1:

Upon a closer examination, you will find that we routinely use FTC-2 to evaluate definite integrals. In this book we will learn to solve problems related to FTC-1, which gives us the rule for differentiating a definite integral. In the remainder of the book, we shall refer to FTC-1, simply as FTC.


---

*<u>Example 1:</u>*

Given that,
$$
g(x) =  \int_{\pi}^{x} t\, \sin^2 t \ dt
$$
find $g'(x).$

<u>*Solution:*</u>

Applying the FTC to differentiate, we get:
$$
g'(x) = x\, \sin^2 x.
$$

---

*<u>Example 2:</u>*

Find the points of local extrema of the function:
$$
F(x) = \int_{0}^{x} (t+1)(t-3)^2 \, dt
$$
*<u>Solution:</u>*

To determine the critical points, we first set the derivative of the function equal to zero.

Applying the FTC to differentiate, we get:
$$
F'(x) = (x + 1) (x - 3)^2
$$
Setting $F'(x) = 0,$ we find that the critical points are $-1$ and $3.$

We can use the *first derivative test* to determine whether each point is a maximum point or a minimum point.

Notice that $(x -3)^2$ is always non-negative. Hence, the sign of $F'(x)$ is determined by the sign of $(x+1).$

We can see that $F'(x)$ is negative to the left of $-1$ and non-negative everywhere else. Therefore, $x = -1$ is a point of local minimum, whereas $x = 3$ is neither maximum, nor minimum.

---

## FTC & Composite Functions

**FTC + Chain Rule**

Let $f$ be continuous in the closed interval $[a, x]$ and let $u = u(x)$ be differentiable wrt to $x,$ then:
$$
\frac{d}{dx}\,  \left(\  \int_{a}^{u} f(t)\, dt \ \right) \ \ = \ \ f(u)\ \dfrac{du}{dx}
$$

### Note 2:

The *FTC + Chain Rule* formula above is easy to prove. Suppose that,
$$
	F(x) =   \int_{a}^{u} f(t)\, dt
$$
where $u = u(x).$ Then applying the chain rule we have,
$$
\dfrac{dF}{dx} \ = \ \dfrac{dF}{du}\ \dfrac{du}{dx}
$$
And then evaluate $\dfrac{dF}{du}$ using the FTC to get:
$$
\dfrac{dF}{dx} \ = \ f(u)\ \dfrac{du}{dx}
$$
Which is equivalent to the required result.


---

*<u>Example 3:</u>*

Evaluate,
$$
\frac{d}{dx}\,  \left(\  \int_{2}^{e^x} \frac{\ln t}{t} \ dt \ \right)
$$
*<u>Solution:</u>*

We apply the *FTC + Chain Rule*. First set,
$$
f(t) = \frac{\ln t}{t} \ \ \mbox{ and } \ \  u(x) = e^x
$$
Hence,
$$
\begin{align*}
\frac{d}{dx}\,  \left(\  \int_{2}^{e^x} \frac{\ln t}{t} \ dt \ \right) \ 	&= \ \ f(u)\ \dfrac{du}{dx} \\[1ex]
													&= \ \ \left( \frac{\ln e^x}{e^x} \right) \left( \dfrac{d\, e^x}{dx} \right) \\[1ex]
													&= \ \ x
\end{align*}
$$
With some practice, we can solve these problems without writing the intermediate steps.

---

*<u>Example 4:</u>*

Find the derivative with respect to $x$ of:
$$
F(x) =  \int_{x}^{2x} (\ln t)^2 \, dt
$$
*<u>Solution:</u>*

We write the function as,
$$
F(x) =  \int_{x}^{a} (\ln t)^2 \, dt \ + \ \int_{a}^{2x} (\ln t)^2 \, dt
$$
where $a > 0$ is an arbitrary constant. We can further write the above as,
$$
F(x) =  -\int_{a}^{x} (\ln t)^2 \, dt \ + \ \int_{a}^{2x} (\ln t)^2 \, dt
$$
and then apply *FTC + Chain Rule* to differentiate and get:
$$
F'(x) \ = \ -(\ln x)^2 + (\ln 2x)^2 \frac{d}{dx}(2x)
$$
Hence,
$$
F'(x) \ = \ 2(\ln 2x)^2 - (\ln x)^2
$$

---

*<u>Example 5:</u>*

Given that,
$$
\int_{0}^{y} e^t\, dt  \ + \int_{0}^{x} \cos t\, dt \ = \ 0
$$
determine $\dfrac{dy}{dx}.$

*<u>Solution:</u>*

Applying the FTC, and differentiating the equation implicitly, we get:
$$
e^y \, \dfrac{dy}{dx} \ + \ \cos x \ = \ 0
$$
Hence,
$$
\dfrac{dy}{dx} \ = \ - e^{-y} \cos x
$$

---

## Practice Problems


1. Find the points of local extrema of the functions:
	1. $\displaystyle{ I(x) = \int_{0}^{x} t e^{-t^2} \, dt \ \ \ }$
			yb-ans
			Local minimum at $x=0.$
			ye-ans
	1. $\displaystyle{ I(x) = \int_{0}^{x} e^{-t}\, (t-3)^2 \, dt \ \ \ }$
			yb-ans
			No local extrema.
			<br><u>*Hint:*</u><br>
			Setting $I'(x) = 0,$ we find that the only critical point is $3.$ However, the first derivative test tells us that $3$ is neither a local maximum, nor a local minimum.
			ye-ans
1. Given that $x > 0,$ find the derivative with respect to $x$ of the following:
	1. $\displaystyle{ F(x) = \int_{x^2}^{x^3} \ln t \, dt \ \ \ }$
			yb-ans
			$\left( 9x^2 - 4x \right) \ln x.$
			ye-ans
	1. $\displaystyle{ F(x) = \int_{\frac{1}{x}}^{\sqrt{x}} \cos \left( t^2 \right) \, dt \ \ \ }$
			yb-ans
			$\dfrac{\cos \left( 1/x^2 \right)}{x^2} + \dfrac{\cos x}{2\sqrt{x}}.$
			ye-ans

1. If $y = f(x)$ is implicitly defined by the following equation,
$$
\int_{0}^{y} \left( t^2 - 2t \right) \, dt \ + \int_{0}^{x^2} \sin t\ dt \ = \ 0
$$
then determine $\dfrac{dy}{dx}$.
	yb-ans
	$\dfrac{-2x \sin \left( x^2 \right)}{y^2 - 2y}.$
	ye-ans

1. If $y = f(x)$ is implicitly defined by the following equation,
$$
\int_{\pi}^{x} \sqrt{2 - \cos^2 z\,} \ dz \ + \int_{y}^{0} e^{-2t}\ dt \ = \ 2
$$
then determine $\dfrac{dy}{dx}$.
	yb-ans
	$e^{2y} \sqrt{2 - \cos^2 x\,}.$
	ye-ans

1. Evaluate the following limits:
	1. $\displaystyle{ \lim_{x \to 0^+}\ \ \frac{\displaystyle \int_{0}^{x^2} \sin \sqrt{t\, } \, dt}{2x^3} }$
			yb-ans
			$\dfrac{1}{3}.$
			<br><u>*Hint:*</u><br>
			Since the given limit is of the $\frac{0}{0}$ form, we can apply L'Hospital's Rule. So, differentiate the numerator and denominator using FTC.
			ye-ans
	1. $\displaystyle{ \lim_{x \to +\infty}\ \ \frac{ \left( \displaystyle \int_{0}^{x} e^{\, t^2} \, dt \right)^2 }{\displaystyle \int_{0}^{x} e^{\, 2t^2} \, dt} }$
			yb-ans
			$0.$
			<br><u>*Hint:*</u><br>
			Since the given limit is of the $\frac{\infty}{\infty}$ form, we can apply L'Hospital's Rule. So, differentiate the numerator and denominator using FTC to get:
			$$
			\begin{align*}
			\lim_{x \to +\infty}\ \frac{ \left( \displaystyle \int_{0}^{x} e^{\, t^2} \, dt \right)^2 }{\displaystyle \int_{0}^{x} e^{\, 2t^2} \, dt} \ &= \ \lim_{x \to +\infty}\ \frac{ 2\left( \displaystyle \int_{0}^{x} e^{\, t^2} \, dt \right) \, e^{\, x^2} }{ e^{\, 2x^2}} \\
							&=	\ \lim_{x \to +\infty}\ \frac{ 2 \displaystyle \int_{0}^{x} e^{\, t^2} \, dt  }{ e^{\, x^2}}
			\end{align*}
			$$
			The limit is once more of the $\frac{\infty}{\infty}$ form, so apply L'Hospital's Rule again!
			ye-ans
