## Introduction

Learn to find the general solution of a **linear differential equation**  of the first order.


## Identify a Linear Differential Equation

A differential equation of the form,
$$
\frac{dy}{dx} + P\, y = Q
$$
is called a **linear differential equation** of the **first order**.

Here, $P = P(x)$ and $Q = Q(x)$ are continuous functions of $x$ (or constants).

### Note 1:

In the remainder of this book, the term *linear differential equation*, refers to a linear differential equation of the <u>first order</u> only.

---

*<u>Example 1:</u>*

Determine whether,
$$
x^2\dfrac{dy}{dx} + y = -3
$$
is a linear differential equation.


<u>*Solution:*</u>

Dividing the given equation by $x^2,$ we have:
$$
\frac{dy}{dx} + \left(\frac{1}{x^2}\right)y = -\frac{3}{x^2}
$$
which is of the form,
$$
\frac{dy}{dx} + P\, y = Q
$$
$$
\text{where, } \ \ \ P = \frac{1}{x^2} \ \ \ \text{ and } \ \ \ Q = -\frac{3}{x^2}
$$
hence it is a linear differential equation.

---

*<u>Example 2:</u>*

Determine whether,
$$
\dfrac{dy}{dx} + x^2 y^2 = \sin x
$$
is a linear differential equation.


<u>*Solution:*</u>

The given differential equation contains a $y^2$ term (rather than a $y$ term) therefore it is <u>not</u> a linear differential equation.


---

*<u>Example 3:</u>*

Determine whether,
$$
\dfrac{dx}{dy} + x y^2 = \cos y
$$
is a linear differential equation.


<u>*Solution:*</u>

The given differential equation is of the form,
$$
\frac{dx}{dy} + P(y)\, x = Q(y)
$$
$$
\text{where, } \ \ \ P(y) = y^2 \ \ \ \text{ and } \ \ \ Q(y) = \cos y
$$

hence it is a linear differential equation in $\frac{dx}{dy}$ and $x$ (but not in $\frac{dy}{dx}$ and $y$).

---


## Solving a Linear Differential Equation

The variables of a linear differential equation are not directly separable. Instead, we can use the following process to find its general solution.

**Step 1**

Put the given linear differential equation in the form,
$$
\frac{dy}{dx} + P\, y = Q
$$
where $P$ and $Q$ are continuous functions of $x$ (or constants).


**Step 2**

Determine $\mathcal{I} = \mathcal{I}(x)$ by evaluating the following integral:
$$
\mathcal{I} = e^{\, \int P\, dx}
$$
This is called an **integrating factor**.


**Step 3**

The general solution of the differential equation can be determined from the following equation:
$$
\mathcal{I}y = \int \mathcal{I}Q\ dx
$$

### Note 2:

Sometimes we will encounter linear differential equations of the form,
$$
\frac{dx}{dy} + P\, x = Q
$$
where $P$ and $Q$ are continuous functions of $y$ (or constants).

This is a linear differential equation in $\dfrac{dx}{dy}$ and $x$ (but not in $\dfrac{dy}{dx}$ and $y$).

In such a case, the integrating factor is given by, $e^{\, \int P\, dy},$ and the general solution is given by:
$$
\mathcal{I}x = \int \mathcal{I}Q\ dy
$$


---

*<u>Example 4:</u>*

Find the general solution of
$$
x\dfrac{dy}{dx} + 3y = x
$$

<u>*Solution:*</u>

Divide by $x$ to rewrite as,
$$
\frac{dy}{dx} + \left(\dfrac{3}{x}\right)y = 1
$$
So, we have $P = \frac{3}{x},$ and $Q = 1.$ Therefore the integrating factor is:
$$
\begin{align*}
\mathcal{I}	&= e^{\, \int \frac{3}{x}\ dx}\\
			&= e^{\, 3\ln x}\\
			&= e^{\, \ln x^3}\\
			&= x^{\, 3}
\end{align*}
$$
We can now find the solution as $\mathcal{I}y = \int \mathcal{I}Q\ dx.$ That is,
$$
\begin{align*}
x^3 \, y	&= \int x^3\ dx\\
		&= \dfrac{x^4}{4} + C
\end{align*}
$$
Therefore, the general solution of the differential equation is
$$
y = \frac{x}{4} + Cx^{-3}
$$

---

*<u>Example 5:</u>*

Find the general solution of
$$
y\, dx + (x - y^3)\, dy = 0
$$

<u>*Solution:*</u>

Rearrange the terms to write in standard form:
$$
\frac{dx}{dy} + \left(\dfrac{1}{y}\right)x = y^2
$$

Notice that the differential equation is of the form $\dfrac{dx}{dy} + Px = Q,$ where $P = \dfrac{1}{y}$ and $Q = y^2.$
So the integrating factor is:
$$
\begin{align*}
\mathcal{I}	&= e^{\, \int \frac{1}{y}\ dy}\\
			&= e^{\, \ln y}\\
			&= y
\end{align*}
$$
We can now find the solution as $\mathcal{I}x = \int \mathcal{I}Q\ dy.$ That is,
$$
\begin{align*}
(y) \, x	&= \int (y)\, y^2 \ dy \\
		&= \int y^3\ dy \\
		&= \dfrac{y^4}{4} + C
\end{align*}
$$
Therefore, the general solution of the differential equation is
$$
x = \dfrac{y^3}{4} + Cy^{-1}
$$


---


## Practice Problems

Solve the differential equation if it is linear. Otherwise mention that it is not a linear differential equation.

1. $\dfrac{dy}{dx} + 4y = e^{-x}$
		yb-ans
		$y = \dfrac{e^{-x}}{3} + Ce^{-4x}$
		ye-ans

1. $xy\ dx - (x^2 + y^2)\ dy = 0$
		yb-ans
		The differential equation is not linear. It is a homogeneous differential equation.
		ye-ans

1. $(1 + x^2)\ dy + 2xy\ dx = \cot x\ dx$
		yb-ans
		$y = \dfrac{\ln|\sin x|}{1 + x^2} + \dfrac{C}{1 + x^2}$
		OR
		$y = \dfrac{\ln|C \sin x|}{1 + x^2}$
		<br><u>*Hint:*</u><br>
		Write the equation as
		$$
		\dfrac{dy}{dx} + \left(\dfrac{2x}{1+x^2}\right) y = \dfrac{\cot x}{1+x^2}
		$$
		ye-ans

1. $y\ dx = (x^2 - y^3)dy$
		yb-ans
		Even though we can write the equation as
		$$
		\dfrac{dx}{dy} - \dfrac{x^2}{y} = -y^2
		$$
		However, the degree of $x$ is $2.$ Therefore it is not a linear differential equation.
		ye-ans

1. $\dfrac{d^2x}{dy^2} + \sin x\ y = \ln y$
		yb-ans
		Not a linear differential equation.
		ye-ans

1. $(\tan^{-1} y - x)\ dy = (1 + y^2)dx$
		yb-ans
		$x = (\tan^{-1}y - 1) + \dfrac{C}{e^{\tan^{-1}y}}$
		<br><u>*Hint:*</u><br>
		Write the equation as
		$$
		\dfrac{dx}{dy} + \dfrac{x}{1+y^2} = \dfrac{\tan^{-1}y}{1+y^2}
		$$
		You will find that the integrating factor is $e^{\tan^{-1}y}.$ So the solution is given by,
		$$
		\left( e^{\tan^{-1}y} \right) x = \int\frac{\tan^{-1}y}{1+y^2} \ e^{\, \tan^{-1}y}\, dy
		$$
		The integral on the RHS can be evaluated by substituting $u = \tan^{-1} y.$  
		ye-ans
