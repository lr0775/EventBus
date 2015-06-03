# EventBus
###   轻量级的EventBus参考greenrobot的EventBus，但没有使用反射，所有事件都只用Event这个类表示，Event中包含事件ID、订阅者列表、处理结果等，事件ID用以区分、分发事件。
