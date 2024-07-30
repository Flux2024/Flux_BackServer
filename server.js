const express = require('express');
const cors = require('cors');

const app = express();
const PORT = 8080;

// CORS 설정
app.use(cors({
    origin: 'http://localhost:8000', // 프론트엔드의 주소
    methods: ['GET', 'POST', 'PUT', 'DELETE'],
    allowedHeaders: ['Content-Type']
}));

app.use(express.json());

// Sample data - assume this is your initial notice
let notices = [
    { noticeId: 1, noticeTitle: 'Sample Notice', noticeContent: 'This is a sample notice.' }
];

// GET all notices
app.get('/notification', (req, res) => {
    res.json(notices);
});


// GET a specific notice by ID
app.get('/notification/:id', (req, res) => {
    const noticeId = parseInt(req.params.id, 10);
    const notice = notices.find(n => n.noticeId === noticeId);
    if (notice) {
        res.json(notice);
    } else {
        res.status(404).send('Notice not found');
    }
});


// POST create a new notice
app.post('/notification', (req, res) => {
    const notice = req.body;

    // Ensure that noticeId is not provided in the request body
    if (notice.noticeId) {
        return res.status(400).send('noticeId should not be provided in the request body');
    }

    // Automatically assign a new ID
    const newId = notices.length ? Math.max(...notices.map(n => n.noticeId)) + 1 : 1;
    notice.noticeId = newId;
    notices.push(notice); // Add new notice to the array
    res.status(201).json(notice); // Return the created notice
});

// PUT update an existing notice
app.put('/notification/:id', (req, res) => {
    const noticeId = parseInt(req.params.id, 10);
    const updatedNotice = req.body;

    // Find the index of the notice to update
    const index = notices.findIndex(notice => notice.noticeId === noticeId);

    if (index !== -1) {
        // Ensure that noticeId is not modified in the request body
        if (updatedNotice.noticeId && updatedNotice.noticeId !== noticeId) {
            return res.status(400).send('noticeId cannot be modified');
        }

        // Update the notice in the array
        notices[index] = { ...notices[index], ...updatedNotice };
        res.json(notices[index]); // Return the updated notice
    } else {
        res.status(404).send('Notice not found');
    }
});

// DELETE an existing notice
app.delete('/notification/:id', (req, res) => {
    const noticeId = parseInt(req.params.id, 10);
    const index = notices.findIndex(notice => notice.noticeId === noticeId);

    if (index !== -1) {
        // Remove the notice from the array
        notices.splice(index, 1);
        res.status(204).send(); // No content
    } else {
        res.status(404).send('Notice not found');
    }
});

app.listen(PORT, () => {
    console.log(`Server is running on http://localhost:${PORT}`);
});
