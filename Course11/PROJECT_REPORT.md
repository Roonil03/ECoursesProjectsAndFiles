# SecureInventoryAPI - Architectural Project Report

## 1. Key Features
- **In-Memory Store Operations**: Rapid simulation of database storage (`mockProductDatabase`) eliminates the need for heavyweight external database infrastructure.
- **Hierarchical Models**: Uses a nested class structural definition linking `ProductItem` entities safely to embedded `ProductCategory` classes.
- **Optimized Caching**: Implementation of `IMemoryCache` on the GET endpoints dramatically accelerates subsequent product listings by intercepting database calls dynamically.

## 2. Security Implementations
- **Authentication & RBAC Simulation**: The `POST /api/products` pipeline mandates the existence of an explicit `UserToken` (simulating a JWT access layer). Following authentication, a strict Role-Based Access Control condition verifies that only identities passing a `UserRole == "Admin"` attribute can append records. 
- **Input Filtering**: Integrated robust structural verification via regular expressions (`Regex(@"^[a-zA-Z0-9\s-_]{3,50}$")`) ensuring no malformed names or negative prices/stock counts enter the persistence layer.
- **XSS Mitigation**: Incorporated `HtmlEncoder.Default.Encode` on all user-supplied textual variables to systematically strip out executable script tag injections (`<script>`).

## 3. Caching Workflows
- **Retrieval Engine**: Evaluates `cache.TryGetValue("CatalogList")` to intercept incoming catalog GET requests.
- **Data Eviction Mechanisms**: Uses dual expiration timers: `AbsoluteExpiration` automatically discards data after 10 minutes guaranteeing synchronization with backend changes, while `SlidingExpiration` evicts entries that haven't been accessed for 2 minutes to conserve memory.
- **Cache Invalidation**: Invokes `cache.Remove("CatalogList")` aggressively inside the POST method to purge stale state data whenever an Admin inserts new structural product records.

## 4. Development Challenges 
- **Decoupled Architecture Testing**: Emulating security contexts without full middleware deployment required shifting validation steps manually into the Minimal API route scope.
- **Type Casting in Caching**: Retrieving un-boxed types required explicitly casting (`out List<ProductItem>?`) during dictionary extraction.

## 5. Logic Structures
- A unified single file `Program.cs` encapsulates modern Minimal API configurations leveraging `Results.Ok`, `Results.Json`, `Results.BadRequest`, and `Results.Created` response wrappers to generate robust HTTP status mapping dynamically.
