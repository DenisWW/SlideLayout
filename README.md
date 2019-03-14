# SlideLayout
Android滑动layout
高仿今日头条导航栏

简单使用与设置（内容使用 ','分割）：

CusRelativeManager cusRelativeManager3 = 
new CusRelativeManager.Builder(CusRelativeLayout,viewPager).addClicTitleViewListener(this).build();

 new CusRelativeManager.CusRelativeLayoutBuilder()
               .setText("我的桌子,我的爱,椅子,板凳,桌子啊,沙发啊,我的天").build(CusRelativeLayout);




混淆注意： -keep class com.ranbell,www.slidelayout.customize.** {*;}
![image](https://github.com/RookieForMingge/LabelForWidAndHei/blob/master/1.png?raw=true)
