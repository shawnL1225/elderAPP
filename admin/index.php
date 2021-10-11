<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>尊嚴長者管理介面</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.0/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-KyZXEAg3QhqLMpG8r+8fhAXLRk2vvoC2f3B09zVXn8CA5QIVfZOJ3BCsw2P0p/We" crossorigin="anonymous">
    <?php
    require "../config.php";
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
                        <a class="btn btn-secondary">活動管理</a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link" href="users.php">使用者</a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link" href="service.php">服務時數</a>
                    </li>
                </ul>
            </div>
        </div>
    </nav>
    <!-- header -->
    <div class="container-fluid pt-4 pb-2 bg-primary bg-opacity-10">
        <h2 class="text-center fw-bold ">
            活動管理
        </h2>
        <p class="text-center text-secondary">在這裡審核活動 、 或取消活動</p>
    </div>
    <!-- event -->
    <div class="container-fluid px-4 py-4">
        <table class="table align-middle shadow">
            <thead class="table-secondary">
                <tr class="text-center fs-5">
                    <th>#</th>
                    <th>圖片</th>
                    <th>標題</th>
                    <th>地址</th>
                    <th>內容</th>
                    <th>日期</th>
                    <th>建立人</th>
                    <th>狀態</th>
                </tr>
                
            </thead>
            <tbody>
                <?php
                     $sql = "SELECT * FROM event ORDER BY id DESC";
                     $stmt = $conn->prepare($sql);
                     $stmt->execute();
                     $result = $stmt->get_result();
                    
                     $cnt = 1;
                     while($row = $result->fetch_assoc()){
                        // check date
                        $date = new DateTime($row['date']);
                        $now = new DateTime();
                
                        if($date < $now) {
                            continue;
                        }
                        $tdStateAttr = "fs-5' data-bs-toggle='modal' data-bs-target='#changeStateModal' onclick='document.querySelector(\"#eid\").value={$row['id']}'";
                        $state = "<td><button class='btn btn-secondary ".$tdStateAttr.">未審核</button></td>";
                        if($row['status'] == 1)
                            $state = "<td><span class='btn btn-success ".$tdStateAttr.">已審核</span></td>";
                        else if($row['status'] == -1)
                            $state = "<td><span class='btn btn-danger ".$tdStateAttr.">已刪除</span></td>";
                        else if($row['status'] == 2)
                            $state = "<td><span class='btn btn-warning ".$tdStateAttr.">已拒絕</span></td>";


                        echo   "<tr class='text-center fs-5'>
                                    <th>{$cnt}</th>
                                    <td style='width:150px'><img src='../imgData/event_img/{$row['id']}.jpg' alt='photo' style='width:150px'></td>
                                    <td>{$row['title']}</td>
                                    <td>{$row['location']}</td>
                                    <td>{$row['content']}</td>
                                    <td>{$row['date']}</td>
                                    <td>{$row['holder']}</td>
                                    {$state}
                                </tr>";
                        $cnt++;
                     }
                     
                ?>
            </tbody>
        </table>
    </div>


    <!-- state modal -->
    <div class="modal fade" id="changeStateModal" tabindex="-1" aria-labelledby="exampleModalLabel" aria-hidden="true">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title" id="exampleModalLabel">更改活動狀態</h5>
                </div>
                <form action="index.php" method="post">
                    <div class="modal-body">
                        <div class="btn-group w-100" role="group" aria-label="Basic radio toggle button group">
                            <input type="radio" class="btn-check" name="stateRadio" id="btnradio1" value="-1" autocomplete="off" checked>
                            <label class="btn btn-outline-danger" for="btnradio1">刪除</label>

                            <input type="radio" class="btn-check" name="stateRadio" id="btnradio2" value="1" autocomplete="off">
                            <label class="btn btn-outline-success" for="btnradio2">同意活動</label>

                            <input type="radio" class="btn-check" name="stateRadio" id="btnradio3"  value="2" autocomplete="off">
                            <label class="btn btn-outline-warning" for="btnradio3">拒絕活動</label>
                        </div>
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">取消</button>
                        <button type="submit"  class="btn btn-primary">確認</button>
                        <input type="hidden" name="eid" value="" id="eid">
                    </div>
                </form>
            </div>
        </div>
    </div>
    <?php
        if(isset($_POST["stateRadio"])){
            $newState = $_POST["stateRadio"];
            $eid = $_POST["eid"];

            $sql = "UPDATE event SET status=? WHERE id=?";
            $stmt = $conn->prepare($sql);
            $stmt->bind_param("ii", $newState, $eid);
            $stmt->execute();
            echo("<meta http-equiv='refresh' content='0.01'>");
        }
    ?>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.0/dist/js/bootstrap.bundle.min.js" integrity="sha384-U1DAWAznBHeqEIlVSCgzq+c9gqGAJn5c/t99JyeKa9xxaYpSvHU5awsuZVVFIhvj" crossorigin="anonymous"></script>
</body>

</html>