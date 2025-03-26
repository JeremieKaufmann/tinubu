import React, { useState, useEffect } from "react";
import { Table, Button, Modal, Form } from "react-bootstrap";
import { getPolicies, createPolicy, updatePolicy } from "./apiService";
import { format } from 'date-fns';

const STATUS_OPTIONS = ["ACTIVE", "INACTIVE"];

const App = () => {
  const [policies, setPolicies] = useState([]);
  const [showModal, setShowModal] = useState(false);
  const [isEditing, setIsEditing] = useState(false);
  const [currentPolicy, setCurrentPolicy] = useState({
    id: null,
    name: "",
    status: "ACTIVE",
    coverageStartDate: "",
    coverageEndDate: ""
  });

  useEffect(() => {
    const fetchPolicies = async () => {
      try {
        const data = await getPolicies();
        setPolicies(data);
      } catch (error) {
        console.error("Erreur lors du chargement des polices:", error);
      }
    };
    fetchPolicies();
  }, []);

  const handleShowAddModal = () => {
    setCurrentPolicy({
      id: null,
      name: "",
      status: "ACTIVE",
      coverageStartDate: "",
      coverageEndDate: ""
    });
    setIsEditing(false);
    setShowModal(true);
  };

  const handleShowEditModal = (policy) => {
    setCurrentPolicy(policy);
    setIsEditing(true);
    setShowModal(true);
  };

  const handleCloseModal = () => setShowModal(false);

  const handleChange = (e) => {
    const { name, value } = e.target;
    setCurrentPolicy((prev) => ({ ...prev, [name]: value }));
  };

  const handleSavePolicy = async () => {
    try {
      if (isEditing) {
        const updated = await updatePolicy(currentPolicy.id, currentPolicy);
        setPolicies(policies.map((p) => (p.id === updated.id ? updated : p)));
      } else {
        const created = await createPolicy(currentPolicy);
        setPolicies([...policies, created]);
      }
      handleCloseModal();
    } catch (error) {
      console.error("Erreur lors de l'enregistrement:", error);
    }
  };

  return (
    <div className="container mt-5">
      <h1>Insurance Policies</h1>

      <Table striped bordered hover>
        <thead>
          <tr>
            <th>ID</th>
            <th>Name</th>
            <th>Status</th>
            <th>Coverage start date</th>
            <th>Coverage end date</th>
            <th>Creation date</th>
            <th>Last update</th>
            <th>Action</th>
          </tr>
        </thead>
        <tbody>
          {policies.map((policy) => (
            <tr key={policy.id}>
              <td>{policy.id}</td>
              <td>{policy.name}</td>
              <td>{policy.status}</td>
              <td>{policy.coverageStartDate ? format(new Date(policy.coverageStartDate), "dd/MM/yyyy") : "—"}</td>
              <td>{policy.coverageEndDate ? format(new Date(policy.coverageEndDate), "dd/MM/yyyy") : "—"}</td>
              <td>{policy.creationDate ? format(new Date(policy.creationDate), "dd/MM/yyyy HH:mm:ss") : "—"}</td>
              <td>{policy.lastUpdateDate ? format(new Date(policy.lastUpdateDate), "dd/MM/yyyy HH:mm:ss") : "—"}</td>
              <td>
                <Button variant="warning" size="sm" onClick={() => handleShowEditModal(policy)}>
                  Edit
                </Button>
              </td>
            </tr>
          ))}
        </tbody>
      </Table>
      <Button variant="primary" onClick={handleShowAddModal} className="mb-3">
        Add a new Insurance Policy
      </Button>


      {/* Modal to add/edit policy */}
      <Modal show={showModal} onHide={handleCloseModal}>
        <Modal.Header closeButton>
          <Modal.Title>{isEditing ? "Update insurance policy" : "Add a new insurance policy"}</Modal.Title>
        </Modal.Header>
        <Modal.Body>
          <Form>
            <Form.Group className="mb-3">
              <Form.Label>Name</Form.Label>
              <Form.Control type="text" name="name" value={currentPolicy.name} onChange={handleChange} />
            </Form.Group>
            <Form.Group className="mb-3">
              <Form.Label>Status</Form.Label>
              <Form.Select name="status" value={currentPolicy.status} onChange={handleChange}>
                {STATUS_OPTIONS.map((status) => (
                  <option key={status} value={status}>
                    {status}
                  </option>
                ))}
              </Form.Select>
            </Form.Group>
            <Form.Group className="mb-3">
              <Form.Label>Coverage start date</Form.Label>
              <Form.Control type="date" name="coverageStartDate" value={currentPolicy.coverageStartDate} onChange={handleChange} />
            </Form.Group>
            <Form.Group className="mb-3">
              <Form.Label>Coverage end date</Form.Label>
              <Form.Control type="date" name="coverageEndDate" value={currentPolicy.coverageEndDate} onChange={handleChange} />
            </Form.Group>
          </Form>
        </Modal.Body>
        <Modal.Footer>
          <Button variant="secondary" onClick={handleCloseModal}>
            Cancel
          </Button>
          <Button variant="primary" onClick={handleSavePolicy}>
            Save
          </Button>
        </Modal.Footer>
      </Modal>
    </div>
  );
};

export default App;
