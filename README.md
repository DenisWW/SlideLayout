# SlideLayout
Android滑动layout
滑块导航栏


设置依赖 implementation 'com.github.DenisWW:SlideLayout:1.0.3'

简单使用与设置（内容使用 ','分割）：

CusRelativeManager cusRelativeManager3 = 
new CusRelativeManager.Builder(CusRelativeLayout,viewPager).addClicTitleViewListener(this).build();

 new CusRelativeManager.CusRelativeLayoutBuilder()
               .setText("我的桌子,我的爱,椅子,板凳,桌子啊,沙发啊,我的天").build(CusRelativeLayout);




混淆注意： -keep class com.ranbell,www.slidelayout.customize.** {*;}
![image](https://github.com/DenisWW/SlideLayout/blob/master/Gif/record-20190314-161647.gif)
