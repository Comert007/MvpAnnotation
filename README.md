# 使用自定义注解实现MVP中Model和View的注入
> 作为一个程序员，我们常常关注我们的代码是否规范和代码的耦合度，不可否认现在很多公司的代码规范已经很规范了，
那么在后面我们关注的点就是降低耦合度，怎样降低耦合度？我想使用注解无疑使最好的！

## 什么是注解（Annotation）
`Annotation` 本身是没有意义的，其地位跟`public`、`private`、`final`等是一样的**元数据**。   
`元数据` 解释数据的数据，就是所谓的配置。

## android中使用注解
不论java还是android中在很多地方，我们已经使用到了注解。

```java 

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
    
```
