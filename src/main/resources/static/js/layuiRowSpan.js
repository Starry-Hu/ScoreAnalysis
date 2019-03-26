/**
 *  name layuiRowspan
 * @param fieldName  要合并列的field属性值
 * @param index 要合并列的索引值 从1开始(如果要合并第一列，则index = 1;)
 * @desc 此方法适用于合并列冻结的单元格
 */
function layuiRowspan(fieldName,index){
    // 左侧列为冻结的情况
    var tbodyNode = document.getElementsByClassName("layui-table-fixed-l")[index-1];
    var child = tbodyNode.getElementsByTagName("td");
    var childFilterArr = [];
    // 获取data-field属性为fieldName的td
    for(var i = 0; i < child.length; i++){
        if(child[i].getAttribute("data-field") == fieldName){
            childFilterArr.push(child[i]);
        }
    }
    // 获取td的个数和种类
    var childFilterTextObj = {};
    for(var i = 0; i < childFilterArr.length; i++){
        var childText = childFilterArr[i].textContent;
        if(childFilterTextObj[childText] == undefined){
            childFilterTextObj[childText] = 1;
        }else{
            var num = childFilterTextObj[childText];
            childFilterTextObj[childText] = num*1 + 1;
        }
    }
    // 给获取到的td设置合并单元格属性
    for(var key in childFilterTextObj){
        var tdNum = childFilterTextObj[key];
        var canRowspan = true;
        for(var i = 0; i < childFilterArr.length; i++){
            if(childFilterArr[i].getAttribute("data-content") == key){
                if(canRowspan){
                    childFilterArr[i].setAttribute("rowspan",tdNum);
                    canRowspan = false;
                }else{
                    childFilterArr[i].style.display = "none";
                }
            }
        }
    }
}
/**
 * name layui合并tbody中单元格的方法
 * @param fieldName  要合并列的field属性值
 * @param index 表格的索引值 从1开始
 * @desc 此方式适用于没有列冻结的单元格合并
 */
function alarmTableRowSpan(fieldName, index) {
    var fixedNode = document.getElementsByClassName("layui-table-body")[index - 1];
    if (!fixedNode) {
        return false;
    }
    var child = fixedNode.getElementsByTagName("td");
    var childFilterArr = [];
    // 获取data-field属性为fieldName的td
    for (var i = 0; i < child.length; i++) {
        if (child[i].getAttribute("data-field") == fieldName) {
            childFilterArr.push(child[i]);
        }
    }
    // 获取td的个数和种类
    var childFilterTextObj = {};
    for (var i = 0; i < childFilterArr.length; i++) {
        var childText = childFilterArr[i].textContent;
        if (childFilterTextObj[childText] == undefined) {
            childFilterTextObj[childText] = 1;
        } else {
            var num = childFilterTextObj[childText];
            childFilterTextObj[childText] = num * 1 + 1;
        }
    }
    // 给获取到的td设置合并单元格属性
    for (var key in childFilterTextObj) {
        var tdNum = childFilterTextObj[key];
        var canRowSpan = true;
        for (var i = 0; i < childFilterArr.length; i++) {
            if (childFilterArr[i].textContent == key) {
                if (canRowSpan) {
                    childFilterArr[i].setAttribute("rowspan", tdNum);
                    canRowSpan = false;
                } else {
                    childFilterArr[i].style.display = "none";
                }
            }
        }
    }
}