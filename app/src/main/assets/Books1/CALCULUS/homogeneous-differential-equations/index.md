## Introduction

Learn to find the general solution of a **homogeneous differential equation**.


## Identify a Homogeneous Differential Equation

A differential equation of the form,
$$
\frac{dy}{dx} = f \left( \frac{y}{x} \right)
$$
is called a **homogeneous differential equation**.

### Homogeneous Functions

(*If you just want to learn to <u>solve</u> homogeneous differential equations, you can skip this section and move on to the next chapter.*)

If a function $f(x, y)$ is such that:
$$
f(\lambda x, \lambda y) = \lambda^n f(x, y)
$$
for any non-zero constant $\lambda,$ then $f$ is said to be a **homogeneous function** of degree $n.$

---

*<u>Example:</u>*

Determine the degree of the following homogeneous functions:

*	$\ f(x, y) \ =\ x^4 + 2x^3y$
*	$\ g(x,y) \ =\ \sin \dfrac{y}{x} + \cos \dfrac{x}{y}$
* $\ u(x, y) \ =\ 2y - 3x$

<u>*Solution:*</u>

We proceed as follows:

*	For $f(x, y),$
$$
f(\lambda x, \lambda y) \ =\ \lambda^4(x^4 + 2x^3y) \ = \ \lambda^4 f( x, y).
$$
Therefore, $f$ is a homogeneous function of degree $4.$

*	For $g(x, y),$
$$g(\lambda x, \lambda y) \ =\ \lambda^0 \left( \sin\dfrac{y}{x} + \cos \dfrac{x}{y}\right) \ =\  \lambda^0 g( x, y).
$$
Therefore, $g$ is a homogeneous function of degree $0.$

* For $u(x, y),$
$$
u(\lambda x, \lambda y) \ =\ \lambda^1 \left( 2y - 3x \right) \ =\  \lambda^1 u( x, y).
$$
Therefore, $u$ is a homogeneous function of degree $1.$

---

A differential equation of the form:
$$
\dfrac{dy}{dx} = F(x, y)
$$
is said to be a **homogeneous differential equation** if $F(x, y)$ is a homogeneous function of <u>degree zero</u>.

Such a differential equation can always be expressed in the form:
$$
\dfrac{dy}{dx} = f\left(\dfrac{y}{x}\right)
$$

Based on the functions examined in the previous example we can therefore say that,
$$
\dfrac{dy}{dx} \ =\ \sin\dfrac{y}{x} + \cos \dfrac{x}{y}
$$
<u>is</u> a homogeneous differential equation.

However,
$$
\dfrac{dy}{dx} \ =\ 2y - 3x
$$
<u>is not</u> a homogeneous differential equation.



## Solving a Homogeneous Differential Equation

You will find that the variables of a homogeneous differential equation are not directly separable. However, a simple substitution allows us to separate the variables and then find a general solution.

**Step 1**

If the given differential equation is homogeneous, then you will be able to express it in the form,
$$
\frac{dy}{dx} = f \left( \frac{y}{x} \right)
$$


**Step 2**

We now make the substitution $v = \dfrac{y}{x}.$ That is to say:
$$
y = vx \ \ \ \text{ and hence,} \ \ \ \ \dfrac{dy}{dx} = v + x\dfrac{dv}{dx}
$$


**Step 3**

When we make the substitution of *Step 2* into the original equation of *Step 1*, then it becomes:
$$
v + x\dfrac{dv}{dx} \ = \ f(v)
$$


**Step 4**

The variables are now easily separable:
$$
\dfrac{dv}{f(v) - v} \ = \ \dfrac{dx}{x}
$$
We can now integrate both sides to obtain a general solution and finally substitute $v = \dfrac{y}{x}$ to get back the original variables.


---

*<u>Example 1:</u>*

Find the general solution of
$$
x^2\dfrac{dy}{dx} = x^2 - y^2 +xy
$$

<u>*Solution:*</u>

Divide by $x^2$ to rewrite as,
$$
\dfrac{dy}{dx} = 1 - \left(\dfrac{y}{x}\right)^2 + \dfrac{y}{x}
$$
The equation is now of the form,
$$
\dfrac{dy}{dx} = f\left(\dfrac{y}{x}\right)
$$
Substitute $y = vx,$ and $\dfrac{dy}{dx} = v + x\dfrac{dv}{dx}$ to get:
$$
\begin{align*}
	v + x\dfrac{dv}{dx}	&= 1 - v^2 + v\\[1em]
	\dfrac{dv}{1 - v^2} 	&= \dfrac{dx}{x}
\end{align*}
$$
Now integrate both sides.
$$
\begin{align*}
	\int\dfrac{dv}{1 - v^2} 								&= \int\dfrac{dx}{x}\\[1em]
	\dfrac{1}{2}\ln\left|\dfrac{1+v}{1-v}\right|					&= \ln |x| + C
\end{align*}
$$
We now substitute $v = \dfrac{y}{x}$ and simplify to write the general solution as:
$$
\dfrac{1}{2}\ln\left|\dfrac{x + y}{x - y}\right| = \ln |x| + C.
$$		

---

*<u>Example 2:</u>*

Find the general solution of
$$
x\sin\left(\dfrac{y}{x}\right)\dfrac{dy}{dx} \ =\ y\sin\left(\dfrac{y}{x}\right) + x
$$


<u>*Solution:*</u>

Rearrange the terms to write the differential equation in the form $\dfrac{dy}{dx} = f\left(\dfrac{y}{x}\right),$
$$
\begin{align*}
	\dfrac{dy}{dx} 	&=  \dfrac{y\sin\left(\dfrac{y}{x}\right) + x}{x\sin\left(\dfrac{y}{x}\right)}\\[1em]
				&= \dfrac{y}{x} + \dfrac{1}{\sin\left(\dfrac{y}{x}\right)}
\end{align*}
$$
Substitute $y = vx$ and, $\dfrac{dy}{dx} = v + x\dfrac{dv}{dx},$ so that,
$$
v + x\dfrac{dv}{dx}	= v + \dfrac{1}{\sin v}
$$
Separate the variables and integrate both sides,
$$
\begin{align*}
	\int\sin v\ dv			&= \int\dfrac{dx}{x}\\[1ex]
	-\cos v				&= \ln |x| + C\\[1ex]
	-\cos\left(\dfrac{y}{x}\right)	&= \ln |x| + C
\end{align*}
$$
Therefore, the general solution of the given differential equation is $-\cos\left(\dfrac{y}{x}\right) = \ln |x| + C.$

---


## Practice Problems

Solve the differential equation if it is homogeneous. Otherwise mention that it is not a homogeneous differential equation.

1. $x^2\ dy = y(x - y)\ dx$
		yb-ans
		$\dfrac{x}{y} = \ln |x| + C$
		ye-ans

1. $xe^{\frac{y}{x}}dy + \left(x - ye^{\frac{y}{x}}\right)\ dx = 0$
		yb-ans
		$e^{\frac{y}{x}} = -\ln|x| + C$
		ye-ans

1. $\cos^2 x\dfrac{dy}{dx} + y = \tan x$
		yb-ans
		The differential equation is not homogeneous, it is a linear differential equation.
		ye-ans

1. $\dfrac{y}{x}\ dx = \cot y - \ln x \ dy$
		yb-ans
		The differential equation is not homogeneous.
		ye-ans

1. $y' = \dfrac{y}{x} + \dfrac{\phi \left(\frac{y}{x} \right)}{\phi ' \left(\frac{y}{x} \right)}$
		yb-ans
		$Cx = \phi \left(\frac{y}{x} \right)$
		<br><u>*Hint:*</u><br>
		Substitute $y = vx$ and rearrange to get:
		$$
		\dfrac{\phi ' (v)\, dv}{\phi (v)} = \frac{dx}{x}
		$$
		Now substitute, $u = \phi (v),$ so that $du = \phi ' (v)\, dv.$
		ye-ans

1. $\dfrac{d^2x}{dy^2} + \dfrac{x}{y} = 3$
		yb-ans
		Not a homogeneous differential equation.  
		ye-ans
