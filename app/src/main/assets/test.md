# After the Big Bang
A brief summary of time
## Life on earth
10 billion years
## You reading this
13.7 billion years

1. Ingredients

    - spaghetti
    - marinara sauce
    - salt

2. Cooking

   Bring water to boil, add a pinch of salt and spaghetti. Cook until pasta is **tender**.

3. Serve

   Drain the pasta on a plate. Add heated sauce.

   > No man is lonely eating spaghetti; it requires so much attention.

   Bon appetit!

| 左对齐 | 右对齐 | 居中对齐 |
| :-----| ----: | :----: |
| 单元格 | 单元格 | 单元格 |
| 单元格 | 单元格 | 单元格 |

分割线

| 左对齐 | 右对齐 | 居中对齐 |
| :-----| ----: | :----: |
| 单元格 | 单元格 | 单元格很多字啊单元格很多字啊单元格很多字啊单元格很多字啊 |
| 单元格 | 单元格 | 单元格 |


```javascript
$(document).ready(function () {
    alert('RUNOOB');
});
```


`printf()` 函数

![](./image_picker3442412251261374084.jpg)


```java

  // Our usage is safe here.
  @SuppressWarnings("PMD.ConstructorCallsOverridableMethod")


  RequestManager(
      Glide glide,
      Lifecycle lifecycle,
      RequestManagerTreeNode treeNode,
      RequestTracker requestTracker,
      ConnectivityMonitorFactory factory,
      Context context) {
    this.glide = glide;
    this.lifecycle = lifecycle;
    this.treeNode = treeNode;
    this.requestTracker = requestTracker;
    this.context = context;
    connectivityMonitor =
        factory.build(
            context.getApplicationContext(),
            new RequestManagerConnectivityListener(requestTracker));

    // If we're the application level request manager, we may be created on a background thread.
    // In that case we cannot risk synchronously pausing or resuming requests, so we hack around the
    // issue by delaying adding ourselves as a lifecycle listener by posting to the main thread.
    // This should be entirely safe.
    if (Util.isOnBackgroundThread()) {
      Util.postOnUiThread(addSelfToLifecycle);
    } else {
      lifecycle.addListener(this);
    }
    lifecycle.addListener(connectivityMonitor);

    defaultRequestListeners =
        new CopyOnWriteArrayList<>(glide.getGlideContext().getDefaultRequestListeners());
    setRequestOptions(glide.getGlideContext().getDefaultRequestOptions());

    glide.registerRequestManager(this);
  }
```

