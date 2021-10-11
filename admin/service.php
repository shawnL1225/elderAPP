<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>尊嚴長者管理介面</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.0/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-KyZXEAg3QhqLMpG8r+8fhAXLRk2vvoC2f3B09zVXn8CA5QIVfZOJ3BCsw2P0p/We" crossorigin="anonymous">
    <?php
    require "../elderSql/config.php";
    ?>
</head>

<body>

    <!-- navbar -->
    <nav class="navbar navbar-expand-md navbar-light py-2 shadow" style="background-color:#FFEFD4">
        <div class="container-xxl">
            <!-- navbar brand / title -->
            <a class="navbar-brand d-flex align-items-center" href="index.php">
                <svg xmlns="http://www.w3.org/2000/svg" width="32" height="32" fill="#B36839" class="bi bi-calendar3-event me-4" viewBox="0 0 16 16">
                    <path d="M14 0H2a2 2 0 0 0-2 2v12a2 2 0 0 0 2 2h12a2 2 0 0 0 2-2V2a2 2 0 0 0-2-2zM1 3.857C1 3.384 1.448 3 2 3h12c.552 0 1 .384 1 .857v10.286c0 .473-.448.857-1 .857H2c-.552 0-1-.384-1-.857V3.857z" />
                    <path d="M12 7a1 1 0 1 0 0-2 1 1 0 0 0 0 2z" />
                </svg>
                <span class="text-secondary fw-bold fs-4">
                    尊嚴長者
                </span>
            </a>
            
            <!-- toggle button for mobile nav -->
            <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#main-nav" aria-controls="main-nav" aria-expanded="false" aria-label="Toggle navigation">
                <span class="navbar-toggler-icon"></span>
            </button>

            <!-- navbar links -->
            <div class="collapse navbar-collapse justify-content-end align-center" id="main-nav">
                <ul class="navbar-nav">
                    <li class="nav-item">
                        <a class="nav-link" href="index.php">活動管理</a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link" href="users.php">使用者</a>
                    </li>
                    <li class="nav-item">
                        <a class="btn btn-secondary">服務時數</a>
                    </li>
                </ul>
            </div>
        </div>
    </nav>
    <!-- header -->
    <div class="container-fluid pt-4 pb-2 bg-primary bg-opacity-10">
        <h2 class="text-center fw-bold ">
            服務時數
        </h2>
        <p class="text-center text-secondary">審核志工的回報資料</p>
    </div>
    <!-- service -->
    <div class="container-fluid px-4 py-4">
        <table class="table table-warning table-striped align-middle shadow">
            <thead class="table-secondary">
                <tr class="text-center fs-5">
                    <th>#</th>
                    <th>名字</th>
                    <th>電話</th>
                    <th>電子郵件</th>
                    <th>日期</th>
                    <th>時間</th>
                    <th>時數</th>
                    <th>內容</th>
                </tr>
                
            </thead>
            <tbody>
                <?php
                     $sql = "SELECT * FROM hour_record INNER JOIN user ON hour_record.uid=user.id ORDER BY hour_record.id DESC";
                     $stmt = $conn->prepare($sql);
                     $stmt->execute();
                     $result = $stmt->get_result();
                    
                     $cnt = 1;
                     while($row = $result->fetch_assoc()){
                        
                        echo   "<tr class='text-center fs-5'>
                                    <th>{$cnt}</th>
                                    <td>{$row['name']}</td>
                                    <td>{$row['phone']}</td>
                                    <td>{$row['email']}</td>
                                    <td>{$row['date']}</td>
                                    <td>{$row['time']}</td>
                                    <td>{$row['hour']}</td>
                                    <td>{$row['content']}</td>
                                </tr>";
                        $cnt++;
                     }
                     
                ?>
            </tbody>
        </table>
    </div>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.0/dist/js/bootstrap.bundle.min.js" integrity="sha384-U1DAWAznBHeqEIlVSCgzq+c9gqGAJn5c/t99JyeKa9xxaYpSvHU5awsuZVVFIhvj" crossorigin="anonymous"></script>
</body>

</html>