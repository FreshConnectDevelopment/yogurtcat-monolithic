# 前言  
spring data jpa提供了Java persist API支持，简化了访问jpa数据源的功能开发。  
- 什么是JPA?  
JPA全称Java持久化访问接口定义，它的作用在于统一了持久层框架的行为，消除不同持久层框架操作的差异性。

# 使用指导
## 数据保存
spring data jpa通过CrudRepository.save(…)方法统一了新增和更新的操作。spring data jpa的save是如何区分新增和更新呢，是通过实体的isNew方法，默认情况是通过id值查询实体是否存在，如果存在调用entityManager.merge(…)进行数据更新，如果不存在调用entityManager.persist(…)进行数据新增。
###  使用举例
```
public interface UserRepository extends Repository&lt;User, Long&gt; {
	
	User save(User user);
}
```
## 数据查询  
spring data jpa数据查询提供了多种方式，这里我们只介绍YogurtCat平台使用的方式。
- 通过方法生成简单查询
- 通过Specifications生产复杂查询
###  通过方法生成简单查询  
方法名查询是spring data jpa提供的一个非常方便的特性。在加载领域对象的时候通过扫描方法名来生成对应的查询语句。  
方法名由操作，主题以及断言三个部分组成。例如findUserByLastnameAndFirstname方法，find是操作，User是主题，LastnameAndFirstname是断言。各个部分的作用如下：
- 操作  
操作包括find,read,get,query以及count。前面四个操作指的是对象查询，最后的count指的是对象数量的查询。
- 主题  
主题就是当前领域对象名，因为可以通过上下文推导，所以主题名一般可以不写。只有一种情况必须写主题那就是与distinct组合去重查询的时候必须携带主题名。
- 断言  
断言就是过滤条件，过来查询结果。
#### 断言关键字及等价的效果见下表
| 关键词  | 示例 | 等价效果 |
| ----- | :--------  | ---------: |
| And     | findByLastnameAndFirstname       | … where x.lastname = ?1 and x.firstname = ?2         |
| Or     | findByLastnameOrFirstname       | … where x.lastname = ?1 or x.firstname = ?2         |
| Is,Equals     | findByFirstname,findByFirstnameIs,findByFirstnameEquals       | … where x.firstname = ?1         |
| Between     | findByStartDateBetween       | … where x.startDate between ?1 and ?2         |
| LessThan     | findByAgeLessThan       | … where x.age &lt; ?1         |
| LessThanEqual     | findByAgeLessThanEqual       | … where x.age &lt;= ?1         |
| GreaterThan     | findByAgeGreaterThan       | … where x.age &gt; ?1         |
| GreaterThanEqual     | findByAgeGreaterThanEqual       | … where x.age &gt;= ?1         |
| After     | findByStartDateAfter       | … where x.startDate &gt; ?1         |
| Before     | findByStartDateBefore       | … where x.startDate &lt; ?1         |
| IsNull     | findByAgeIsNull       | … where x.age is null         |
| IsNotNull,NotNull     | findByAge(Is)NotNull       | … where x.age not null         |
| Like     | findByFirstnameLike       | … where x.firstname like ?1         |
| NotLike     | findByFirstnameNotLike       | … where x.firstname not like ?1         |
| StartingWith     | findByFirstnameStartingWith       | … where x.firstname like ?1 (parameter bound with appended %)         |
| EndingWith     | findByFirstnameEndingWith       | … where x.firstname like ?1 (parameter bound with prepended %)         |
| Containing     | findByFirstnameContaining       | … where x.firstname like ?1 (parameter bound wrapped in %)         |
| OrderBy     | findByAgeOrderByLastnameDesc       | … where x.age = ?1 order by x.lastname desc         |
| Not     | findByLastnameNot       | … where x.lastname &lt;&gt; ?1         |
| In     | findByAgeIn(Collection&lt;Age&gt; ages)       | … where x.age in ?1         |
| NotIn     | findByAgeNotIn(Collection&lt;Age&gt; ages)       | … where x.age not in ?1         |
| True     | findByActiveTrue()       | … where x.active = true         |
| False     | findByActiveFalse()       | … where x.active = false         |
| IgnoreCase     | findByFirstnameIgnoreCase       | … where UPPER(x.firstame) = UPPER(?1)         |

###  使用举例
```
public interface UserRepository extends Repository&lt;User, Long&gt; {
	
	User findByUsername(String username);
}
```
###  通过Specifications生产复杂查询
通过方法名自动生成查询虽然是一个便利的方法，但是如果查询条件复杂的话，整个方法就会特别的复杂，这个时候就需要使用Specifications生产复杂查询。  
Specifications实现步骤如下：
1. 继承JpaSpecificationExecutor接口
2. 创建Specification实现类，构建查询条件  
Specifications能够支持复杂查询就在于Specification接口，本节将会重点讲解这部分。  
```
public interface Specification&lt;T&gt; extends Serializable {

	static &lt;T&gt; Specification&lt;T&gt; not(Specification&lt;T&gt; spec) {
		return Specifications.negated(spec);
	}

	static &lt;T&gt; Specification&lt;T&gt; where(Specification&lt;T&gt; spec) {
		return Specifications.where(spec);
	}

	default Specification&lt;T&gt; and(Specification&lt;T&gt; other) {
		return Specifications.composed(this, other, AND);
	}

	default Specification&lt;T&gt; or(Specification&lt;T&gt; other) {
		return Specifications.composed(this, other, OR);
	}

	@Nullable
	Predicate toPredicate(Root&lt;T&gt; root, CriteriaQuery&lt;?&gt; query, CriteriaBuilder criteriaBuilder);
}
```
上面是Specification接口的代码描述，Specification有五个方法下面我们将会讲解每个方法的作用以及使用场景。
- static &lt;T&gt; Specification&lt;T&gt; not(Specification&lt;T&gt; spec)  
不满足spec参数指定的条件构建，类似于！=spec的作用，使用示例如下
```
Specification<Object> specification = Specification
				.not(serializableSpec.and(serializableSpec).or(serializableSpec));
```
- static &lt;T&gt; Specification&lt;T&gt; where(Specification&lt;T&gt; spec)
提供了一个创建Specification对象的工厂方法
```
Specification<Object> specification = Specification.where(null);;
```
- default Specification&lt;T&gt; and(Specification&lt;T&gt; other)  
将other条件添加到当前对象中，是一个与关系，两个条件都要满足。
```
Specification<Object> specification = where(null);
		specification = specification.and(spec);
```

- default Specification&lt;T&gt; or(Specification&lt;T&gt; other)  
将other条件添加到当前对象中，是一个或关系，两个条件满足一个就可以。
```
Specification<Object> specification = where(null);
		specification = specification.or(spec);
```

- Predicate toPredicate(Root&lt;T&gt; root, CriteriaQuery&lt;?&gt; query, CriteriaBuilder criteriaBuilder)  
使用root和query构建查询条件
```
Specification<Object> specification = where(null);
		specification = specification.or(spec);
```


1. 使用步骤2.构建的条件，进行复杂查询

###  使用举例
- 继承JpaSpecificationExecutor接口
```
public interface UserRepository extends Repository&lt;User, Long&gt;, JpaSpecificationExecutor&lt;User&gt; {
	
}
```
- 创建Specification实现类，构建查询条件
```
public class UserSpec implements Specification&lt;User&gt; {
	
	private static final long serialVersionUID = 1L;
	
	/**
	 * 条件
	 */
	@Builder.Default
	private Optional&lt;UserVo&gt; condition = Optional.empty();

	@Override
	public Predicate toPredicate(Root&lt;User&gt; root, CriteriaQuery&lt;?&gt; query, CriteriaBuilder criteriaBuilder) {
		List&lt;Predicate&gt; predicates = new ArrayList&lt;Predicate&gt;();
		if(condition.isPresent()) {
			if(!StringUtil.isNullOrEmpty(condition.get().getUsername())) {
				predicates.add(criteriaBuilder.equal(root.get("username"), condition.get().getUsername()));
			}
			if(!StringUtil.isNullOrEmpty(condition.get().getPassword())) {
				predicates.add(criteriaBuilder.equal(root.get("password"), condition.get().getPassword()));
			}
			if(!StringUtil.isNullOrEmpty(condition.get().getToken())) {
				predicates.add(criteriaBuilder.equal(root.get("token"), condition.get().getToken()));
			}
			return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
		}else {
			return null;
		}
	}

}
```
- 复杂查询
```
public UserVo findUser(String name, String password) {
    Optional&lt;UserVo&gt; condition = Optional.ofNullable(UserVo.builder().username(name).password(password).build());
    Optional&lt;User&gt; user = userRepository.findOne(UserSpec.builder().condition(condition).build());
    if (!user.isPresent()) {
        return null;
    }
    UserVo target = new UserVo();
    BeanUtils.copyProperties(user.get(), target);
    return target;
}
```
