## Introduction

In this book, we will learn to evaluate various types of **limits** by applying **L'Hospital's Rule**

We will see that applying this rule dramatically simplifies the evaluation of many seemingly complicated limits.



## The L'Hospital Rule

Once we are familiar with differentiation, we can use the following rule to evaluate several limits.

**L'Hospital's Rule**

If the functions $f(x)$ and $g(x)$ are differentiable in some neighbourhood of point $a$ (except maybe at $a$) and satisfy one of the following conditions:

*	${\displaystyle \lim_{x \to a} f(x) \ = \ \lim_{x \to a} g(x) \ = \  0 \ \ }$ **or**,
* ${\displaystyle \lim_{x \to a} f(x) \ = \ \lim_{x \to a} g(x) \ = \  \infty }$

then,
$$
\lim_{x \to a} \ \frac{f(x)}{g(x)} \ \ = \ \ \lim_{x \to a} \ \frac{f'(x)}{g'(x)}
$$
provided ${\displaystyle \lim_{x \to a} \frac{f'(x)}{g'(x)} \ }$ exists, and $g'(x) \ne 0.$

The point $a$ may be finite, or $+\infty$ or, $-\infty.$

This rule is useful in solving limits of several different types of **indeterminate forms**.

We classify limits of the so called, indeterminate forms, in the following categories:

<u>Type-I</u>

$$
\frac{0}{0} \ \ \ \text{ or } \ \ \ \frac{\infty}{\infty}
$$

<u>Type-II</u>

$$
0 \cdot \infty \ \ \ \text{ or } \ \ \ \infty - \infty
$$

<u>Type-III</u>

$$
1^{\infty} \ \ \ \ \ \text{ or } \ \ \ \ \ \infty^{0} \ \ \ \ \ \text{ or } \ \ \ \ \ 0^{0}
$$

In the following chapters we will learn to evaluate limits of each of these types through several examples.


## Indeterminate Forms of Type I

L'Hospital's Rule can be directly applied to indeterminate forms of the type:
$$
\frac{0}{0} \ \ \ \text{ or } \ \ \ \frac{\infty}{\infty}
$$

---

*<u>Example 1:</u>*

$$
\lim_{x \to a} \, \dfrac{x^n - a^n}{x - a} \ \ \ \ \text{ where, } n \in \mathbb{Z^{+}}
$$

<u>*Solution:*</u>

This limit is of the $\frac{0}{0}$ form and satisfies the conditions of L'Hospital's rule, therefore we differentiate the numerator & denominator to proceed as follows:
$$
\begin{align*}
\lim_{x \to a} \, \dfrac{x^n - a^n}{x - a} \ &= \ \lim_{x \to a} \, \dfrac{nx^{n-1} - 0}{1 - 0} \\[1ex]
&= \ na^{n-1}
\end{align*}
$$

---

*<u>Example 2:</u>*

$$
\lim_{x \to 0} \, \dfrac{e^{2x} + e^{-2x} - 2}{1 - \cos x}
$$

<u>*Solution:*</u>

This limit is of the $\frac{0}{0}$ form and satisfies the conditions of L'Hospital's rule, so we differentiate the numerator & denominator to get:
$$
\lim_{x \to 0} \, \dfrac{e^{2x} + e^{-2x} - 2}{1 - \cos x} \ = \ \lim_{x \to 0} \, \dfrac{2e^{2x} - 2e^{-2x}}{\sin x}
$$
The resulting limit is once again of the $\frac{0}{0}$ form so we can apply L'Hospital's rule again to get:
$$
\lim_{x \to 0} \, \dfrac{2e^{2x} - 2e^{-2x}}{\sin x} \ = \ \lim_{x \to 0} \, \dfrac{4e^{2x} + 4e^{-2x}}{\cos x} = \ 8.
$$

Hence,
$$
\lim_{x \to 0} \, \dfrac{e^{2x} + e^{-2x} - 2}{1 - \cos x} \ = \ 8.
$$

---


## Indeterminate Forms of Type II

Indeterminate forms of the type,
$$
0 \cdot \infty \ \ \ \text{ or } \ \ \ \infty - \infty
$$
can often be reduced to Type I by some algebraic manipulation.

---

*<u>Example 3:</u>*

$$
\lim_{x \to 0^+} \ x^n \ln x \ \ \ \ (n > 0)
$$

<u>*Solution:*</u>

This limit is of the $0 \cdot \infty$ form, so we transform it to the form $\frac{\infty}{\infty}$ by re-writing as:
$$
\lim_{x \to 0^+} \ x^n \ln x \ = \ \lim_{x \to 0^+} \ \frac{\ln x}{x^{-n}}
$$
In this form, the limit now satisfies the conditions of L'Hospital's rule, so we differentiate the numerator & denominator to get:
$$
\lim_{x \to 0^+} \  \frac{(1/x)}{-n x^{-n-1}}
$$
Since $n > 0,$ this can be re-written and evaluated as:
$$
\lim_{x \to 0^+} \ \frac{-x^n}{n} \ = \ 0
$$

---

*<u>Example 4:</u>*

$$
\lim_{x \to 0} \ \cot x - \frac{1}{x}
$$

<u>*Solution:*</u>

This limit is of the form $\infty - \infty,$ so we convert it to the form $\frac{0}{0}$ as follows:
$$
\begin{align*}
\lim_{x \to 0} \ \cot x - \frac{1}{x} \ &= \ \lim_{x \to 0} \ \frac{1}{\tan x} - \frac{1}{x} \\[1ex]
&= \ \lim_{x \to 0} \ \frac{x - \tan x}{x\tan x}
\end{align*}
$$
It is now in the $\frac{0}{0}$ form and satisfies the conditions of L'Hospital's rule. We differentiate the numerator \& denominator to re-write as:
$$
\lim_{x \to 0} \ \frac{1 - \sec^2 x}{\tan x + x\sec^2 x}
$$
Once more, we get a limit of the $\frac{0}{0}$ form, and therefore we apply L'Hospital's rule again. Differentiating the numerator \& denominator we get:
$$
\lim_{x \to 0} \ \frac{2\sec^2 x \, \tan x}{2\sec^2 x  + 2x\sec^2 x \, \tan x} \ = \ \frac{0}{2} \ = \ 0.
$$

---


## Indeterminate Forms of Type III

Indeterminate forms of the type,
$$
1^{\infty} \ \ \ \ \ \text{ or } \ \ \ \ \ \infty^{0} \ \ \ \ \ \text{ or } \ \ \ \ \ 0^{0}
$$
can be reduced to the form $0 \cdot \infty$ by using exponential and logarithmic identities.

Suppose that you have $u^v$ in one of the above indeterminate forms. Use the definition of the logarithm to re-write the function as:
$$
u^v = e^{v\ln u}
$$
(You can take logs on both sides to verify this identity)

Alternatively, set $\, {\displaystyle k = \lim_{x \to a}\, u^v },$ then take logs:
$$
\begin{align*}
	\ln k \ &= \ \ln \left( \lim_{x \to a}\, u^v  \right) \\[1ex]
			&= \lim_{x \to a}\, \ln \left( u^v  \right) \\[1ex]
			&= \ \lim_{x \to a}\, v \, \ln u
\end{align*}
$$
In either case, the *exponent* is converted to a *product* that is solvable as a Type II problem.

---

*<u>Example 5:</u>*

$$
\lim_{x \to 0^+} \ (\sin x)^{\tan x}
$$

<u>*Solution:*</u>

This limit is of the form $0^0.$ We therefore set
$$
k = \lim_{x \to 0^+} \, (\sin x)^{\tan x}
$$
and take logs to get:
$$
\begin{align*}
		\ln k \ &=	\ \lim_{x \to 0^+} \ \ln (\sin x)^{\tan x} \\
				&=	\ \lim_{x \to 0^+} \ \tan x \, \ln (\sin x)
\end{align*}
$$
This is now of the form $0 \cdot \infty,$ so we convert it to the $\frac{\infty}{\infty}$ form as follows:
$$
\lim_{x \to 0^+} \ \tan x \, \ln (\sin x) \ = \ \lim_{x \to 0^+} \ \frac{\ln (\sin x)}{\cot x}
$$
The resulting limit satisfies the conditions of L'Hospital's rule, so we differentiate the numerator \& denominator to write:
$$
\lim_{x \to 0^+} \ \frac{\ln (\sin x)}{\cot x} \ = \ \lim_{x \to 0^+} \ \frac{\cot x}{-\text{cosec}^2 x}
$$
But, we know that ${\displaystyle \frac{\cot x}{-\text{cosec}^2 x} = -\sin \, x \cos x }.$

Hence,
$$
\ln k \ = \ \lim_{x \to 0^+} \ -\sin x \, \cos x \ = \ 0
$$
Since $ \ln k = 0 ,$ it follows that $ k = 1$ and we can finally write:
$$
\lim_{x \to 0^+} \ (\sin x)^{\tan x} = 1.
$$

<u>*Alternate Solution:*</u>

We can also solve the problem by instead using the identity $u^v = e^{v\ln u}.$ We would then proceed as follows:
$$
\begin{align*}
	\lim_{x \to 0^+} \ (\sin x)^{\tan x} \ &=	\ \lim_{x \to 0^+} e^{\, \tan x \, \ln (\sin x)}
\end{align*}
$$
We now proceed to evaluate ${\displaystyle \lim_{x \to 0^+} \tan x \, \ln (\sin x)}$ as per the earlier solution to get:
$$
\lim_{x \to 0^+} \ \tan x \, \ln (\sin x) \ = \ 0
$$
and therefore we have:
$$
\lim_{x \to 0^+} \ (\sin x)^{\tan x} \ = \ e^0 \ = \ 1.
$$

---


## When <u>not</u> to use L'Hospital's Rule

L'Hospital's Rule is a powerful tool to evaluate limits, but we should also know when <u>not</u> to apply it!

---

*<u>Example 6:</u>*

$$
\lim_{x \to \infty} \ \frac{x + \sin x}{x}
$$

*<u>Solution:</u>*

We see that the given limit is of the $\frac{\infty}{\infty}$ form, therefore, applying L'Hospital's Rule, we get:
$$
\lim_{x \to \infty} \ \frac{x + \sin x}{x} \ = \ \lim_{x \to \infty} \ (1 + \cos x)
$$
But since ${\displaystyle \lim_{x \to \infty} \ (\cos x )}$ does not exist, we see that the limit on the RHS does not exist.

At this point we conclude that **L'Hospital's rule does not apply** to this limit. We need to solve it by an alternative method.

This is because, if you examine the statement of L'Hospital's rule, you will see that given a limit of the $\frac{0}{0}$ or $\frac{\infty}{\infty}$ form, we can apply the rule to evaluate ${\displaystyle \lim_{x \to a}} \, \frac{f(x)}{g(x)}$ **provided that** ${\displaystyle \lim_{x \to a}}\, \frac{f'(x)}{g'(x)}$ **exists**.

In this case, we can instead solve the problem as follows:
$$
\begin{align*}
	\lim_{x \to \infty} \ \frac{x + \sin x}{x} \ &= \ \lim_{x \to \infty} \ \left( 1 + \frac{\sin x}{x} \right) \\
								&= \ 1 + \lim_{x \to \infty} \, \frac{\sin x}{x} \\
								&= \ 1 + 0 \\
								&= \ 1
\end{align*}
$$

We see that the limit, in fact exists, and is equal to $1.$

---

### Note:

We can use the *squeeze theorem* or *sandwich theorem* to evaluate ${\displaystyle \lim_{x \to \infty} \, \frac{\sin x}{x} }$ since we know that as $x$ increases without bound, the following is always true:
$$
\frac{-1}{x} \ \ \le \ \ \frac{\sin x}{x} \ \ \le \ \ \frac{1}{x}
$$
Now, applying the limit as $x \to \infty$ we get:
$$
0 \ \ \le \ \ \lim_{x \to \infty} \frac{\sin x}{x} \ \ \le \ \ 0.
$$



## Practice Problems

Use L'Hospital's rule to evaluate the following limits.

1. ${\displaystyle \lim_{x \to a} \, \dfrac{x^n - a^n}{x^3 - a^3} },$ where $n > 3$ is an integer, and $a \ne 0.$
		yb-ans
		$\dfrac{na^{n-3}}{3}.$
		ye-ans

1. ${\displaystyle \lim_{x \to 0} \  \frac{\ln (2 + x) - \ln 2}{x} }$
		yb-ans
		$\dfrac{1}{2}.$
		ye-ans

1. ${\displaystyle \lim_{x \to 0} \, \dfrac{e^x - e^{-x} - 2x}{x - \sin x} }$
		yb-ans
		$2.$
		<br><u>*Hint:*</u><br>
		Apply L'Hospital's rule three times!
		ye-ans

1. ${\displaystyle \lim_{x \to \frac{\pi}{2}} \  \left( \frac{\pi}{2} - x \right) \ \tan x }$
		yb-ans
		$1.$
		<br><u>*Hint:*</u><br>
		The given limit is of the form $( 0 \cdot \infty ),$ so re-write as $\frac{\frac{\pi}{2} - x}{\cot x}$ to convert to the $\frac{0}{0}$ form.
		ye-ans

1. ${\displaystyle \lim_{x \to 1} \ \left( \dfrac{1}{\ln x} - \dfrac{1}{x - 1} \right) }$
		yb-ans
		$\dfrac{1}{2}.$
		<br><u>*Hint:*</u><br>
		The given limit is of the form $\infty - \infty,$ so convert to the $\frac{0}{0}$ form and then apply L'Hospital's rule (twice).
		ye-ans

1. ${\displaystyle \lim_{x \to 0} \ (\cos x)^{\, \text{cosec } x} }$
		yb-ans
		$1.$
		<br><u>*Hint:*</u><br>
		The limit is of the form $1^{\infty},$ so set the given limit equal to $k$ and take logs.<br>
		Alternatively, re-write as:
		$$
		(\cos x)^{\, \text{cosec } x} \ = \ e^{ \, \text{cosec } x \, \ln (\cos x)  }
		$$
		Then evaluate $\text{cosec} \, x \, \ln (\cos x)\,$ as $x \to 0$ by re-writing as $\frac{\ln (\cos x)}{\sin x},$ which is in $\frac{0}{0}$ form.  
		ye-ans

1. ${\displaystyle \lim_{x \to 0} \ (\cos x + \sin x)^{\frac{1}{x}} }$
		yb-ans
		$e.$
		<br><u>*Hint:*</u><br>
		Similar to problem 6.
		ye-ans
