### 📌 **JPA, QueryDSL, MyBatis 문법 비교**
JPA, QueryDSL, MyBatis를 각각 어떻게 사용하는지 **기본 CRUD 문법과 동적 쿼리 작성 방식**을 비교하면서 설명해 드릴게요.

---

## **1️⃣ JPA 기본 문법**
**JPA는 객체 중심으로 DB를 다루는 ORM 프레임워크**입니다.  
쿼리를 직접 작성하지 않아도, **메서드 네이밍 규칙(Query Method)이나 JPQL을 활용하여 데이터를 조회**할 수 있습니다.

### ✅ **JPA 기본 CRUD**
```java
// JPA 엔티티
@Entity
@Table(name = "users")
@Getter @Setter
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_name")
    private String name;

    @Column(name = "user_email")
    private String email;
}
```

```java
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    // 1️⃣ 기본 메서드 (JpaRepository 제공)
    Optional<User> findById(Long id);
    List<User> findAll();
    void deleteById(Long id);

    // 2️⃣ Query Method 사용
    List<User> findByName(String name);
    List<User> findByEmailContaining(String email);
}
```

✔ **JPA에서는 기본적으로 `JpaRepository`를 상속받으면 CRUD 기능이 자동 제공됨**  
✔ **메서드 네이밍 규칙(Query Method)으로도 다양한 조회 기능 구현 가능**

---

## **2️⃣ QueryDSL 문법**
QueryDSL은 **JPA를 기반으로 한 타입 안전한 쿼리 빌더**입니다.  
✔ `BooleanBuilder`를 사용하여 동적 쿼리를 쉽게 작성할 수 있습니다.  
✔ `JPQL`보다 가독성이 좋고, 유지보수가 편리합니다.

### ✅ **QueryDSL 기본 사용법**
```java
@RequiredArgsConstructor
@Service
public class UserService {

    private final JPAQueryFactory queryFactory;

    public List<User> searchUsers(String name, String email) {
        QUser user = QUser.user; // QueryDSL 엔티티

        return queryFactory
            .selectFrom(user)
            .where(
                name != null ? user.name.eq(name) : null,
                email != null ? user.email.contains(email) : null
            )
            .fetch();
    }
}
```

✔ **QueryDSL은 `QUser`라는 클래스를 자동 생성하여 사용**  
✔ **JPQL보다 동적 쿼리를 더 쉽게 작성할 수 있음**  
✔ **타입 안전성을 보장하여 IDE에서 자동완성이 가능**

---

## **3️⃣ MyBatis 문법**
MyBatis는 **SQL을 직접 제어할 수 있는 프레임워크**입니다.  
✔ XML 기반 또는 `@Mapper`를 사용하여 SQL을 작성할 수 있음  
✔ 복잡한 SQL을 직접 최적화할 때 유리

### ✅ **MyBatis 기본 CRUD**
📌 **1) XML 방식**
```xml
<!-- resources/mapper/UserMapper.xml -->
<mapper namespace="com.example.mapper.UserMapper">
    <select id="findUserById" parameterType="long" resultType="User">
        SELECT * FROM users WHERE id = #{id}
    </select>

    <insert id="insertUser" parameterType="User">
        INSERT INTO users (user_name, user_email) VALUES (#{name}, #{email})
    </insert>

    <delete id="deleteUser" parameterType="long">
        DELETE FROM users WHERE id = #{id}
    </delete>
</mapper>
```
```java
@Mapper
public interface UserMapper {
    User findUserById(@Param("id") Long id);
    void insertUser(User user);
    void deleteUser(@Param("id") Long id);
}
```

📌 **2) @Mapper 방식**
```java
@Mapper
public interface UserMapper {
    @Select("SELECT * FROM users WHERE id = #{id}")
    User findUserById(@Param("id") Long id);

    @Insert("INSERT INTO users (user_name, user_email) VALUES (#{name}, #{email})")
    void insertUser(User user);

    @Delete("DELETE FROM users WHERE id = #{id}")
    void deleteUser(@Param("id") Long id);
}
```

✔ **MyBatis는 SQL을 직접 작성할 수 있어 튜닝이 용이**  
✔ **복잡한 쿼리를 다룰 때 JPA보다 유리함**  
✔ **XML 방식과 어노테이션 방식 둘 다 사용 가능**

---

## **4️⃣ JPA vs QueryDSL vs MyBatis 비교**
| 비교 항목 | JPA | QueryDSL | MyBatis |
|----------|----|---------|--------|
| **쿼리 작성 방식** | JPQL, Query Method | Java 코드로 빌더 방식 작성 | SQL 직접 작성 |
| **타입 안전성** | X (문자열 기반) | O (타입 안전) | X (SQL 직접 작성) |
| **동적 쿼리 지원** | X (JPQL에서 불편) | O (BooleanBuilder 사용) | O (XML에서 if, choose 사용) |
| **성능 최적화** | 자동 최적화 | JPA 기반 최적화 | SQL 튜닝 가능 |
| **가독성 및 유지보수** | O | O | X (SQL이 많아지면 복잡) |
| **사용 목적** | 기본 CRUD, 단순 조회 | 복잡한 동적 쿼리 | 성능 최적화, 복잡한 SQL |

---

## **🎯 결론: 언제 어떤 걸 사용해야 할까?**
### ✅ **JPA (기본 선택)**
✔ 기본적인 CRUD 및 단순 조회 쿼리 사용  
✔ Query Method로 간단한 검색 기능 구현 가능  
✔ 데이터 변경 트랜잭션 관리가 필요할 때 적합

### ✅ **QueryDSL (JPA의 부족한 점 보완)**
✔ **동적 쿼리 작성이 필요한 경우**  
✔ **타입 안전성이 필요할 때**  
✔ **JPQL보다 가독성을 개선하고 싶을 때**  
✔ JPA를 유지하면서 복잡한 검색 조건을 구현해야 할 때

### ✅ **MyBatis (SQL 튜닝이 필요할 때)**
✔ **JPA의 성능 최적화가 어렵거나, N+1 문제가 심할 때**  
✔ **복잡한 SQL을 직접 작성해야 할 때**  
✔ **특정 DBMS의 기능을 활용해야 할 때 (예: Stored Procedure, Window Function 등)**

✔ **대부분의 프로젝트에서 JPA + QueryDSL을 기본으로 사용하고,**  
✔ **성능이 중요한 부분만 MyBatis로 최적화하는 것이 좋은 선택입니다.**

---

## **📌 정리**
✅ **JPA → 기본적인 CRUD 처리**  
✅ **QueryDSL → 동적 쿼리 및 복잡한 검색 조건**  
✅ **MyBatis → SQL 튜닝 및 성능 최적화 필요할 때**

프로젝트 요구사항에 맞게 **JPA + QueryDSL을 기본으로 사용하고, 성능 최적화가 필요하면 MyBatis를 활용