# 使用自定义注解实现MVP中Model和View的注入
> 作为一个程序员，我们常常关注我们的代码是否规范和代码的耦合度，不可否认现在很多公司的代码规范已经很规范了，
那么在后面我们关注的点就是降低耦合度，怎样降低耦合度？我想使用注解无疑使最好的！

## 什么是注解（Annotation）
在Java1.5以后，引入了注解，也称作元数据    
`Annotation` 本身是没有意义的，其地位跟`public`、`private`、`final`等是一样的**元数据**。   


## android中使用注解
android在很多地方，我们已经使用到了注解。比如：

```java 

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
    
```
在上面的代码中，我们就可以看到，我们使用了`@Override`注解，就是表示覆盖重写。

### 常见的注解   
* `Override`  表示该方法是重写父类中的方法，编译的时候会检查该方法，如果这个方法不是父类中存在的将会报错
* `Deprecated` 表示该方法时已经过时的，表示该方法有风险或者有更好的替代方法
* `SuppressWarnings` 表示在编译的时候忽略某种错误，如版本检查等
* `Nullable` 表示可以为空
* `NonNull` 表示不能为空

## 元注解
元注解就是基本注解，在自定义注解中我们都会使用到元注解，元注解主要有以下四个：

### @Retention

这个注解表示注解的保留方式，有如下三种：

* `SOURCE`：只保留在源码中，不保留在class中，同时也不加载到虚拟机中
* `CLASS`：保留在源码中，同时也保留到class中，但是不加载到虚拟机中
* `RUNTIME`：保留到源码中，同时也保留到class中，最后加载到虚拟机中

一般使用最多的还是最后一种：  

```java

@Retention(RetentionPolicy.RUNTIME)
public @interface RetentionAnnotation {
   int action();
}

```

### @Target

这个注解表示注解的作用范围，主要有如下:

* `ElementType.FIELD` 注解作用于变量
* `ElementType.METHOD` 注解作用于方法
* `ElementType.PARAMETER` 注解作用于参数
* `ElementType.CONSTRUCTOR` 注解作用于构造方法
* `ElementType.LOCAL_VARIABLE` 注解作用于局部变量
* `ElementType.PACKAGE` 注解作用于包

### @Inherited

是否可以被继承，默认为 `false`

### @Documented

是否会保存到 Javadoc 文档中

**Tip：在常用的4中元注解中，常常使用的是前两种，也就是`@Retention` 和 `@Target`**

## 自定义注解

> 在当前项目中，我们就使用了自定义注解 `@Layout` 和 `@MVAnnotation`，通过这两个实现对mvp中view和model的注入。这个项目中，将activity当作了一个presenter.

### @Layout

#### 定义@Layout
在我们开发Android的时候，经常会写一个类似与`BaseActivity`（继承于`Activity`）的基类，然后在里面做一些初始化操作。
一般都会使用一个抽象方法返回布局view。那么我们是否可以将返回的布局通过注解实现呢？

在开始前，我们得思考：我们定义的注解返回什么？ 
返回的就是一个布局的`id`，那么我们定义的注解返回就是int。

所以自定义注解`@Layout` 方法中就返回一个int类型的`id`；

```java

@Retention(RetentionPolicy.RUNTIME)
public @interface Layout {
   int layoutResId();
}

```

#### 实现@Layout

前面说过了，注解没有任何的实际操作，仅仅修饰数据。那么我们肯定需要定义注解的操作。     
这就需要我们在我们的`BaseActivity`（这里是`PresenterActivity`）中使用了

```java

Class<? extends PresenterActivity> aClass = this.getClass();
Layout annotation = aClass.getAnnotation(Layout.class);
if (annotation != null) {
   int layoutResId = annotation.layoutResId();
   setContentView(layoutResId);
}

```

在上面我们通过获取这个`PresenterActivity`的子类获取子类上面的注解`annotation`，然后通过注解得到这个布局`Id`
再将得到的布局通过`setContentView(layoutResId)`就可以向`Activity`加入布局

#### 使用@Layout

怎么使用呢？其实很简单了：

在继承 `PresenterActivity`的子类上加上注解就可以了
```java
@Layout(layoutResId = R.layout.activity_login)
public class LoginActivity extends PresenterActivity{
...
...
}
```

### @MVAnnotation

#### 定义@MVAnnotation
在我们的`mvp`模式开发中，我们不仅仅需要布局，还需要`model`和`view`，如何通过注解来实现呢？
我们还是考虑一个问题：我们需要的`modle`和`view`是什么类型？当然是`Object`，那么我们自定义注解的时候是返回一个Object？
其实返回的是一个`Class`。通过`Class` 我们不久可以生成想要的对象了么！所以做出如下定义：

```java
@Retention(RetentionPolicy.RUNTIME)
public @interface MVAnnotation {
    Class getModelClz();

    Class getViewClz();
}

```

#### 实现@MVAnnotation

实现方法跟上面基本一样：

```java
IView view = null;
Object model = null;

Class<? extends PresenterActivity> aClass = this.getClass();
MVAnnotation annotation = aClass.getAnnotation(MVAnnotation.class);

String canonicalV = annotation.getViewClz().getCanonicalName();
String canonicalM = annotation.getModelClz().getCanonicalName();

try {
     view = (IView) Class.forName(canonicalV).newInstance();
     Class modelClz = Class.forName(canonicalM);
     if (modelClz.getClass().isAssignableFrom(IModel.class)) {
         model = modelClz.newInstance();
     } else {
         model = createRetrofitService(modelClz);
     }

} catch (ClassNotFoundException e) {
       e.printStackTrace();
} catch (InstantiationException e) {
       e.printStackTrace();
} catch (IllegalAccessException e) {
       e.printStackTrace();
}

```

#### 使用@MVAnnotation

```java
@MVAnnotation(getModelClz = LoginModel.class,getViewClz = LoginView.class)
@Layout(layoutResId = R.layout.activity_login)
public class LoginActivity extends PresenterActivity<LoginView,LoginModel> {
...
...
}

```

**总结：**

* **自定义注解使用到了元注解；**
* **自定义注解要讲究三步走，即：定义->实现->使用**

查看项目，请戳[这里](https://insight.io/github.com/sourfeng/AOPTest/tree/master/)