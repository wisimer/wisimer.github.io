---
date: 2014-09-23
title: Make a .9.png
layout: default
category: ANDROID
---

.9.png is so MAGICAL and How to Make a .9.png

#### Step 1:make a circle in photoshop like this:

![circle.png](/src/imgs/1409/23_circle.png)
Picture-1:circle.png

#### Step 2:run draw9patch.sh in sdk/tools/

open the circle.png,then draw four line at the border,like this:

![draw9patch.png](/src/imgs/1409/23_draw9patch.png)
Picture-2:draw9patch.png

<!--more-->

REAMRKS:

```java
Top Line:stretch the place below it when horizonal drawing;
Left Line:stretch the place at the right of the line when vertical drawing;
Right Line:show the content at the left of the line when vertival drawing;
Bottom Line:show the content above it when horizontal drawing;
```

#### Step 3:use the .9.png in project:

    	<TextView android:layout_width="match_parent"
	        android:layout_height="60dp"
	        android:text="pull"
	        android:id="@+id/puu_head"
	        android:gravity="center"
	        android:background="@drawable/ic"/>

then the final effect like this:

![9effect.png](/src/imgs/1409/23_9effect.png)
Picture-3:9effect.png

### And more remarkable,to be effective only use the attribute `android:background`.If you set the .9.png as `android:src` ,there is no effect unfortunately. ###

- - -
THE END.
