const addCarModal = document.getElementById('addCarModal');
const closeModal = document.getElementById('closeModal');
const insertCarButton = document.getElementById('insertCar');
const addCarBtn = document.getElementById('addCarBtn');

// 打开弹窗
insertCarButton.addEventListener('click', function() {
    addCarModal.style.display = 'block';
});

// 关闭弹窗
closeModal.addEventListener('click', function() {
    addCarModal.style.display = 'none';
});
