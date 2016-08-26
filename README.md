# Dagger2Demo
这两天一直在网上学习Dagger2，看到很多关于Dagger2的文章，其实大体都一样。下来又是标签，又是代码的；然后我创建一个工程，把代码拷贝过去并且运行。我靠，都是红色的bug，一头雾水，特别是DaggerXXXActivityComponent这个类怎么得来，不得而知。

言归正传，学习Dagger2需要一点预备知识：

1. Java注解
http://www.cnblogs.com/peida/archive/2013/04/24/3036689.html
当然，个人觉得简单的把Java注解理解为一种标注也是可以的。

2. Java依赖注入
http://www.cnblogs.com/devinzhang/p/3862942.html
http://www.2cto.com/kf/201303/194527.html
简单来说，就是代码中用到的对象，不是使用new语句生成的，而是通过别的方式获得对象实例。

下面的描述主要是Dagger2的配置和简单入门（下面描述的同时，一步一步配置与实现简单例子）。
Dagger2是Google维护，与Dagger1还是有些区别。而且，Dagger2虽然使用了注解，但并不是使用Java发射机制来实现（反射性能是一个我问题），而是编译的时候，生成辅助代码来实现的。

1 既然是编译生成的辅助代码，那么需要生成代码的工具，Android Studio本身并不具有该功能。
![image](https://cloud.githubusercontent.com/assets/7099994/17962212/5eae4e74-6ae2-11e6-8dae-82427d90b875.png)
这里首先创建了一个全新Android工程，接着配置Project的build.gradle(注意非app的build.gradle)。Dagger2自动生成代码使用的代码生成工具是android-apt，使用的是1.8版本，注意这个工具后续应该会不断更新。
配置语句为: classpath 'com.neenbedankt.gradle.plugins:android-apt:1.8'

2 在配置完代码生成工具之后，我们需要在app的build.gradle中配置使用Dagger2需要的包：
![image](https://cloud.githubusercontent.com/assets/7099994/17963071/59df1c8a-6ae6-11e6-904a-b08c8d35c89e.png)
这里需要引入的有两个地方：
第一引入apt插件，语句apply plugin: 'com.neenbedankt.android-apt'；这句必须配置，否则即使配置步骤1中的apt，也不会自动生成代码。
第二导入Dagger2所需要的三个包，注意使用Google的2.X版本的Dagger，语句
compile 'com.google.dagger:dagger:2.6.1'
compile 'com.google.dagger:dagger-compiler:2.6.1'
compile 'org.glassfish:javax.annotation:10.0-b28'
至此，使用Dagger2的工具与框架包设置完毕，可以正式使用Dagger2.

3 使用Dagger2进行对象注入，首先需要在目标的变量中，标上@Inject注解
![image](https://cloud.githubusercontent.com/assets/7099994/17963331/b3aab138-6ae7-11e6-992e-a0a3cb1f04fe.png)
如上图Person类的对象mPerson，标上了@Inject注解，注意不能使用private定义。而进行对象注入，有两种方式：

3.1 第一种方式，使用@Inject标注构造函数的方式。即在Person的构造方法，标上@Inject。
![image](https://cloud.githubusercontent.com/assets/7099994/17963467/4eac9fc0-6ae8-11e6-97ec-28278c40a73a.png)
这时候，你会发现需要注入的对象mPerson，以及标注Person构造函数，在操作的逻辑上已经完成匹配，理应能够获得mPerson的字段信息。因此，在MainActivity的onCreate方法中添加语句Log.e(TAG, mPerson.getName())来进行测试。结果发现抛出了NullPointerException，也就是注入失败。

3.2 上面注入失败，是因为需要标明哪个Activity（或者Application等）需要进行依赖注入。为了标明哪个Activity需要注入，需要做两步工作：
1）添加标注了@Component的接口，注意inject方法这个写法。
![image](https://cloud.githubusercontent.com/assets/7099994/17991767/4322c580-6b74-11e6-8064-678cbeffe627.png)
2）设置注入Activity。
![image](https://cloud.githubusercontent.com/assets/7099994/17991964/d6f51ef6-6b75-11e6-893a-9e3449ce33a4.png)
在写该语句的过程中，会发现没有DaggerMainActivityComponent类，这时候只要build一下，这个类就会出现在app的build目录中
![image](https://cloud.githubusercontent.com/assets/7099994/17991991/132d4b6e-6b76-11e6-9512-9f780754f30b.png)
完成3.1和3.2，在运行一下代码，这时候发现注入成功！

3.3 除了使用3.1中构造函数的方式进行注入外，还有另外一种优先级更高的注入方式，实现该注入需要进行三个步骤：
1）撰写Module类，如下：
![image](https://cloud.githubusercontent.com/assets/7099994/17992228/2d306c92-6b78-11e6-8b83-201b5c16a4fa.png)
这里有几点需要说明一下，首先该类需要标上@Module注解；另外，需要提供一个生成Person的方法，该方法标上@Provides；方法名一般为provideXXX，但是并不是必须的，只要有一个唯一标注了@Provides并且返回参数为Person即可。
2）修改Component接口，如下：
![image](https://cloud.githubusercontent.com/assets/7099994/17992319/c42893cc-6b78-11e6-83ff-b786e783f771.png)
这里主要是在@Component注解中添加Module类。这样Component接口就在逻辑上连接Activity与Module类了。
3）修改Activity中，配置注入的语句：
![image](https://cloud.githubusercontent.com/assets/7099994/17992359/0a8120dc-6b79-11e6-9b45-d237cc5f6f5e.png)
这里，找不到builder()后的方法personModule(new PersonModule())，只要rebuild一下，该方法就会被生成。至此，第二种注入方式完成，运行一下会发现，输出改为“John”。

3.4 注入优先级问题。如果配置了两种注入方式，Dagger2会首先选择Module类的方式。

3.5 对于Module类中，出现自定义对象参数的问题。如果出现这种情况，还是会根据上面两种方式的优先级来进行递归注入。如下：
![image](https://cloud.githubusercontent.com/assets/7099994/17993929/168a66a0-6b87-11e6-811c-c74f8717ccc9.png)
![image](https://cloud.githubusercontent.com/assets/7099994/17992855/68c08454-6b7d-11e6-886e-67732ad5de46.png)
![image](https://cloud.githubusercontent.com/assets/7099994/17992860/741b79f8-6b7d-11e6-9f55-fe1537b7a8d6.png)
![image](https://cloud.githubusercontent.com/assets/7099994/17992870/810afdaa-6b7d-11e6-9411-50eea9b2c7e5.png)
最后一幅图中，注意@Component中的modules的写法，使用数组的方式定义注入所使用到的Module类。


到这里，Dagger2的配置与入门例子撰写完毕。下面简单介绍一下几个常用注解、以及一些的逻辑。

1 注解

@Inject 注解主要两方面的作用，首先标注需要注入的目标属性，第二是使用在构造函数上为目标属性提供依赖。

@Provides 标注一个方法，该方法可以在需要提供依赖时被调用，从而把预先提供好的对象当做依赖给标注了@Inject的变量赋值。@Provides主要用于Module类中的方法

@Module 用Module标注的类是专门用来提供依赖的。对于自定义的类，我们可以使用@Inject注解进行构造函数进行标注，简单；对于第三方定义的类，无法在构造函数进行处理，那么只能使用@Module的方式提供注入的依赖。

@Component 标注接口，被标注了Component的接口在编译时会产生相应的类的实例来作为提供依赖方和需要依赖方之间的桥梁，把相关依赖注入到其中。

除了上面4个最常用的注解之外，还有一些高级用法的注解，例如@Qualifier、@Scope、@SubComponent ，大家可以搜搜学习一下，这里就不一一展开介绍了。


2 Activity(或者Application)、Component和Module之间的关系。使用@Inject标注构造函数的方式很好理解；对于使用Module类注入的来说，Component作为桥接功能，使得Module类与Activity关联起来。
![image](https://cloud.githubusercontent.com/assets/7099994/17993873/72cb15fa-6b86-11e6-90f5-ac157966f622.png)

除了上面的功能，Dagger2还可以MVP架构整合在一齐，以后有时间，我将会继续介绍这方面的功能。
