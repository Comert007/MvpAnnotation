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

