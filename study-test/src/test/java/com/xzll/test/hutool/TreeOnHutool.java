package com.xzll.test.hutool;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.tree.Tree;
import cn.hutool.core.lang.tree.TreeNode;
import cn.hutool.core.lang.tree.TreeNodeConfig;
import cn.hutool.core.lang.tree.TreeUtil;
import com.alibaba.fastjson.JSON;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Auther: Huangzhuangzhuang
 * @Date: 2021/7/18 09:40
 * @Description:  使用hutool工具类生成标准 json树形结构 比如菜单 角色 等比较适用 get了
 */
public class TreeOnHutool {


	/**
	 * 使用默认的字段名称 (一般情况下其实也足够了 因为有拓展字段)
	 * <p>
	 * 构建树结构的json (标准树结构) 字段都是默认的 具体见这个类 {@link cn.hutool.core.lang.tree.TreeNodeConfig}
	 * <p>
	 * 构建出的节点字段 如下这样 其中 extra2 为自定义的字段 是map ，也就是说可以有多个自定义字段
	 * <p>
	 * {
	 * "id":"3",
	 * "parentId":"2",
	 * "weight":0,
	 * "name":"黄壮壮 2",
	 * "extra2":"extra_value2",
	 * "children":[
	 * {
	 * "id":"4",
	 * "parentId":"3",
	 * "weight":0,
	 * "name":"黄壮壮 3",
	 * "extra3":"extra_value3"
	 * }
	 * ]
	 */
	@Test
	public void config() {

		List<TreeNode<String>> nodeList = new ArrayList<>();

		for (int i = 0; i < 4; i++) {
			TreeNode<String> node = new TreeNode<>();
			Map<String, Object> map = new HashMap<>();
			map.put("extra" + i, "extra_value" + i);
			map.put("extra" + i, "extra_value" + i);
			map.put("extra" + i, "extra_value" + i);
			TreeNode<String> treeNode = node.setId(String.valueOf(i + 1)).setName("黄壮壮 " + i).setParentId(String.valueOf(i)).setExtra(map);
			nodeList.add(treeNode);
		}

		List<Tree<String>> treeNodes = TreeUtil.build(nodeList, "0");
		String s = JSON.toJSONString(treeNodes);
		System.out.println(s);

		/*
		[{"id":"1","parentId":"0","weight":0,"name":"黄壮壮 0","extra0":"extra_value0","children":[{"id":"2","parentId":"1","weight":0,"name":"黄壮壮 1","extra1":"extra_value1","children":[{"id":"3","parentId":"2","weight":0,"name":"黄壮壮 2","extra2":"extra_value2","children":[{"id":"4","parentId":"3","weight":0,"name":"黄壮壮 3","extra3":"extra_value3"}]}]}]}]
		*/
	}

	/**
	 * 自定义字段名称
	 */
	@Test
	public void customizeFieldName() {

		// 构建node列表
		List<TreeNode<String>> nodeList = CollUtil.newArrayList();
		nodeList.add(new TreeNode<>("1", "0", "系统管理", 5));
		nodeList.add(new TreeNode<>("11", "1", "用户管理", 222222));
		nodeList.add(new TreeNode<>("111", "11", "用户添加", 0));
		nodeList.add(new TreeNode<>("2", "0", "店铺管理", 1));
		nodeList.add(new TreeNode<>("21", "2", "商品管理", 44));
		nodeList.add(new TreeNode<>("221", "2", "商品管理2", 2));


		//配置
		TreeNodeConfig treeNodeConfig = new TreeNodeConfig();
		// 自定义属性名 都要默认值的
		treeNodeConfig.setChildrenKey("子节点");
		treeNodeConfig.setWeightKey("自定义某个字段");
		treeNodeConfig.setIdKey("主键ID");
		treeNodeConfig.setNameKey("自定义某个name");
		// 最大递归深度
		//treeNodeConfig.setDeep(3);

		//转换器
		List<Tree<String>> treeNodes = TreeUtil.build(nodeList, "0", treeNodeConfig,
				(treeNode, tree) -> {
					tree.setId(treeNode.getId());
					tree.setParentId(treeNode.getParentId());
					tree.setWeight(treeNode.getWeight());
					tree.setName(treeNode.getName());
					// 扩展属性 ...
					tree.putExtra("拓展属性1", "拓展属性1value");
					tree.putExtra("拓展属性2", "拓展属性2value");
				});

		System.out.println(JSON.toJSONString(treeNodes));
		/*
		输出:

		[
    {
        "主键ID":"2",
        "parentId":"0",
        "自定义某个字段":1,
        "自定义某个name":"店铺管理",
        "拓展属性1":"拓展属性1value",
        "拓展属性2":"拓展属性2value",
        "子节点":[
            {
                "主键ID":"221",
                "parentId":"2",
                "自定义某个字段":2,
                "自定义某个name":"商品管理2",
                "拓展属性1":"拓展属性1value",
                "拓展属性2":"拓展属性2value"
            },
            {
                "主键ID":"21",
                "parentId":"2",
                "自定义某个字段":44,
                "自定义某个name":"商品管理",
                "拓展属性1":"拓展属性1value",
                "拓展属性2":"拓展属性2value"
            }
        ]
    },
    {
        "主键ID":"1",
        "parentId":"0",
        "自定义某个字段":5,
        "自定义某个name":"系统管理",
        "拓展属性1":"拓展属性1value",
        "拓展属性2":"拓展属性2value",
        "子节点":[
            {
                "主键ID":"11",
                "parentId":"1",
                "自定义某个字段":222222,
                "自定义某个name":"用户管理",
                "拓展属性1":"拓展属性1value",
                "拓展属性2":"拓展属性2value",
                "子节点":[
                    {
                        "主键ID":"111",
                        "parentId":"11",
                        "自定义某个字段":0,
                        "自定义某个name":"用户添加",
                        "拓展属性1":"拓展属性1value",
                        "拓展属性2":"拓展属性2value"
                    }
                ]
            }
        ]
    }
]

		 */
	}
}
